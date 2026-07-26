# PFT-003 — PostgreSQL connection and Flyway migration setup

**Epic:** EPIC-01 Foundations
**Status:** backlog
**Size:** S
**Prerequisites:** PFT-001

## Purpose

Wire up a local PostgreSQL database and Flyway so schema changes are versioned from the very first table.

## Why it matters

Schema evolution is guaranteed on this project. Versioned migrations from day one mean the database can always be rebuilt from empty, which also makes the deploy story simple later.

## Tasks

- [ ] Provision a local Postgres (Docker Compose is fine) and document it in the README
- [ ] Add JDBC/HikariCP config for the `default` datasource
- [ ] Add the Flyway sbt or Play module and point it at `conf/db/migration`
- [ ] Configure migrations to run on startup in dev
- [ ] Prove a trivial migration applies and is recorded in `flyway_schema_history`

## Acceptance criteria

- [ ] App starts against a completely empty database and migrates itself
- [ ] Re-running the app does not re-apply migrations
- [ ] Connection details come from env vars with dev defaults

## Notes / decisions

- Docker Compose keeps local setup reproducible and matches how production Postgres will be managed.

## Implementation hints

- Naming convention: `V1__create_users.sql`, `V2__...`. Never edit an applied migration; add a new one.
