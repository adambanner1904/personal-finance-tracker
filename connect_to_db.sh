#!/usr/bin/env bash
set -e

# 1. Always attempt to start Colima; it no-ops if already running
echo "Ensuring Colima is running..."
colima start || echo "Colima start failed or already running; continuing..."

# 2. Start Postgres via docker-compose if it's not running
if ! nc -z localhost 5432 2>/dev/null; then
  echo "Postgres is not reachable on localhost:5432. Starting docker-compose..."
  docker-compose up -d
else
  echo "Postgres is already reachable on localhost:5432."
fi

# 3. Connect to Postgres
echo "Connecting to Postgres..."
PGPASSWORD=finance psql -h localhost -p 5432 -U pft_app -d personal_finance_tracker
