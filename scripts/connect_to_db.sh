#!/usr/bin/env bash
set -e

# Set up db
scripts/db-up.sh

# Connect to Postgres
echo "Connecting to Postgres..."
PGPASSWORD=finance psql -h localhost -p 5432 -U pft_app -d personal_finance_tracker
