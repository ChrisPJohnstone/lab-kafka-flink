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
	mkdir -p output
	chmod 777 output
	$(CONTAINER_ENGINE) cp $(FLINK_JOB_JAR) flink-jobmanager:/tmp/flink-job.jar
	$(CONTAINER_ENGINE) exec flink-jobmanager flink run -d --class $(FLINK_JOB_MAIN) /tmp/flink-job.jar

destroy:
	make -C generator clean
	make -C flink-job clean
	$(CONTAINER_ENGINE) compose down -v
	$(CONTAINER_ENGINE) unshare rm -rf output
