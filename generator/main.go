package main

import (
	"fmt"
	"math/rand/v2"
	"time"

	"github.com/google/uuid"
)

const letters = "abcdefghijklmnopqrstuvwxyz"

type record struct {
	time time.Time
	id   string
	name string
	age  int
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
		time: time.Now(),
		id:   uuid.NewString(),
		name: randString(rand.IntN(25)),
		age:  rand.IntN(100),
	}
}

func main() {
	record := newRecord()
	fmt.Printf("%s\n", record)
}
