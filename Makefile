CONTAINER_ENGINE ?= podman
FLINK_JOB_JAR ?= flink-job/target/flink-job-0.1.0.jar
FLINK_JOB_MAIN ?= batcher.Batcher

.PHONY: all deploy topic generator destroy jar job

all: topic generator

deploy:
	$(CONTAINER_ENGINE) compose up -d

topic: deploy
	$(CONTAINER_ENGINE) exec kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server "localhost:9092" --topic lab --create

generator:
	make -C generator

jar: deploy
	make -C flink-job

job: jar
	$(CONTAINER_ENGINE) cp $(FLINK_JOB_JAR) flink-jobmanager:/tmp/flink-job.jar
	$(CONTAINER_ENGINE) exec -it flink-jobmanager flink run -c $(FLINK_JOB_MAIN) /tmp/flink-job.jar

destroy:
	make -C generator clean
	make -C flink-job clean
	$(CONTAINER_ENGINE) compose down -v
