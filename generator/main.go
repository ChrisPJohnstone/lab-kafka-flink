package main

import (
	"encoding/json/v2"
	"fmt"
	"math/rand/v2"
	"time"

	"github.com/google/uuid"
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
	for {
		record := newRecord()
		builder, err := json.Marshal(record)
		if err != nil {
			panic(err)
		}
		fmt.Printf("%s\n", string(builder))
	}
}
