#!/usr/bin/env bash
set -e

# 1. Always attempt to start docker desktop; it no-ops if already running
echo "Ensuring Docker Desktop is running..."
docker desktop start --detach || echo "Docker Desktop start failed or is already running; continuing..."

echo "Using the Docker Desktop context..."
unset DOCKER_HOST
docker context use desktop-linux

# 2. Start Postgres via docker-compose if it's not running
if ! nc -z localhost 5432 2>/dev/null; then
  echo "Postgres is not reachable on localhost:5432. Starting docker-compose..."
  docker-compose up -d
else
  echo "Postgres is already reachable on localhost:5432."
fi

# 3. Migrate test db as it unsets whenever we compose it down
scripts/flyway-migrate.sh