CONTAINER_ENGINE ?= podman
FLINK_JOB_JAR    ?= flink-job/target/flink-job-0.1.0.jar
FLINK_JOB_MAIN   ?= batcher.Batcher

.PHONY: all deploy topic generator jar job destroy

all: topic generator jar

deploy:
	$(CONTAINER_ENGINE) compose up -d

topic: deploy
	$(CONTAINER_ENGINE) exec kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server "localhost:9092" --topic lab --create

generator:
	make -C generator

jar:
	make -C flink-job

job: jar topic
	$(CONTAINER_ENGINE) cp $(FLINK_JOB_JAR) flink-jobmanager:/tmp/flink-job.jar
	$(CONTAINER_ENGINE) exec -it flink-jobmanager flink run -c $(FLINK_JOB_MAIN) /tmp/flink-job.jar

destroy:
	make -C generator clean
	make -C flink-job clean
	$(CONTAINER_ENGINE) compose down -v
