package main

import (
	"context"
	"encoding/json/v2"
	"math/rand/v2"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/google/uuid"
	"github.com/segmentio/kafka-go"
)

const letters = "abcdefghijklmnopqrstuvwxyz"

type record struct {
	Time time.Time `json:"time"`
	Id   string    `json:"id"`
	Name string    `json:"name"`
	Age  int       `json:"age"`
}

func randString(length int) string {
	builder := make([]byte, length)
	for i := range builder {
		builder[i] = letters[rand.IntN(len(letters))]
	}
	return string(builder)
}

func newRecord() record {
	return record{
		Time: time.Now(),
		Id:   uuid.NewString(),
		Name: randString(rand.IntN(25)),
		Age:  rand.IntN(100),
	}
}

func main() {
	writer := &kafka.Writer{
		Addr:     kafka.TCP("localhost:9092"),
		Topic:    "lab",
		Balancer: &kafka.LeastBytes{},
	}
	defer writer.Close()
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	sig := make(chan os.Signal, 1)
	signal.Notify(sig, syscall.SIGINT, syscall.SIGTERM)
	go func() {
		<-sig
		cancel()
	}()
	for {
		record := newRecord()
		builder, err := json.Marshal(record)
		if err != nil {
			panic(err)
		}
		err = writer.WriteMessages(ctx, kafka.Message{Value: builder})
		if err != nil {
			panic(err)
		}
		select {
		case <-ctx.Done():
			return
		case <-time.After(time.Second):
		}
	}
}
