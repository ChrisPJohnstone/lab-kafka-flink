CONTAINER_ENGINE ?= podman

.PHONY: all deploy topic generator destroy

all: topic generator

deploy:
	$(CONTAINER_ENGINE) compose up -d

topic: deploy
	$(CONTAINER_ENGINE) exec kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server "localhost:9092" --topic lab --create

generator:
	make -C generator

destroy:
	make -C generator clean
	$(CONTAINER_ENGINE) compose down -v
