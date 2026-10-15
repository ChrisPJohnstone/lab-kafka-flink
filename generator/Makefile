BINARY           ?= generator
CONTAINER_ENGINE ?= podman

.PHONY: clean lint

$(BINARY): $(shell find . -type f -name '*.go')
	go build -o $(BINARY) .

clean:
	rm -f $(BINARY)

lint:
	$(CONTAINER_ENGINE) run --rm -v "$(PWD):/app:Z" -w /app \
		docker.io/golangci/golangci-lint:v2.14.0 \
		golangci-lint run
