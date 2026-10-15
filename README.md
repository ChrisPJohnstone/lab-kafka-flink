# Kafka & Flink Lab

A lab project to help me learn how kafka & flink work

## Usage

### Pre-Requisites

- Golang
- A container compose method e.g. `podman compose` or `docker compose`

### Quickstart

There are full details below but this is the fastpath to results

```sh
make -C kafka topic
make -C generator run &
podman exec kafka /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic lab \
  --from-beginning
```

#### Kafka

- Create container
  ```sh
  make -C kafka
  ```
- Create topic
  ```sh
  make -C kafka topic
  ```
- Destroy container
  ```sh
  make -C kafka destroy
  ```

#### Generator

- Compile
  ```sh
  make -C generator
  ```
- Execute
  ```sh
  make -C generator run
  ```
- Clean up
  ```sh
  make -C generator clean
  ```

## Roadmap

- [x] Data Generator
- [x] Kafa Setup
- [ ] Flink Setup
