CONTAINER_ENGINE ?= podman

.PHONY: deploy destroy topic

deploy:
	$(CONTAINER_ENGINE) compose up -d

destroy:
	$(CONTAINER_ENGINE) compose down -v

topic: deploy
	$(CONTAINER_ENGINE) exec kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server "localhost:9092" --topic lab --create
