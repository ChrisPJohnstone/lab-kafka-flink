# Kafka & Flink Lab

A lab project to help me learn how kafka & flink work

## Usage

### Pre-Requisites

- Golang
- Java 11 and Maven
- A container compose method e.g. `podman compose` or `docker compose`

### Quickstart

- Start Containers, Compile Generator & Start Flink Job
  ```sh
  make
  ```
- Start generating data
  ```sh
  ./generator/generator
  ```
- See data being written to output/ dir
  ```sh
  ls -larH output/
  ```
- Destroy Containers and binaries
  ```sh
  make destroy
  ```

## Roadmap

- [x] Data Generator
- [x] Kafa Setup
- [x] Flink Setup
  - [x] Reading from Kafka using datastream
  - [x] Writing to files
  - [x] Batching up Records
