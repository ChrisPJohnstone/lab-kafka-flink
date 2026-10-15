# Kafka & Flink Lab

A lab project to help me learn how kafka & flink work

## Usage

### Pre-Requisites

- Golang
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
- Destroy Containers and binaries
  ```sh
  make destroy
  ```

## Roadmap

- [x] Data Generator
- [x] Kafa Setup
- [ ] Flink Setup
