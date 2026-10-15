CONTAINER_ENGINE ?= podman

.PHONY: up down

up:
	$(CONTAINER_ENGINE) compose up -d

down:
	$(CONTAINER_ENGINE) compose down -v
