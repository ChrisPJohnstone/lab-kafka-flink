# Kafka & Flink Lab

A lab project to help me learn how kafka & flink work

## Usage

### Pre-Requisites

- Golang
- Java 11 and Maven
- A container compose method e.g. `podman compose` or `docker compose`

### Quickstart

- Start Containers
  ```sh
  make
  ```
- Start generating data
  ```sh
  ./generator/generator
  ```
- Start flink job (currently outputs to screen)
  ```sh
  make job
  ```
- Destroy Containers and binaries
  ```sh
  make destroy
  ```

## Roadmap

- [x] Data Generator
- [x] Kafa Setup
- [x] Flink Setup
