# PFT-006 — Doobie transactor, repository package and query conventions

**Epic:** EPIC-01 Foundations
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-003, PFT-005

## Purpose

Set up the Doobie transactor, the persistence package layout, shared conventions, and a database health check.

## Why it matters

This is the seam between the app and the database. Establishing conventions once — how transactions are handed to services, how enums are mapped, how ownership is enforced in SQL — prevents every repository being written in a different style.

## Tasks

- [ ] Add Doobie dependencies (`doobie-core`, `doobie-hikari`, `doobie-postgres`)
- [ ] Build a `Transactor` from Play config, provided via DI at application scope
- [ ] Create `persistence` package with one repository trait+impl per aggregate
- [ ] Add a `DbHealth` check running `SELECT 1` and expose it on `/health`
- [ ] Add shared `Meta`/`Get`/`Put` instances for the account type and category enums
- [ ] Document the convention: repositories return `ConnectionIO`, services compose and run transactions

## Acceptance criteria

- [ ] `/health` returns 200 when the DB is up and a clear failure when it is not
- [ ] A repository method can be called end to end from a controller
- [ ] Enum columns round-trip to Scala ADTs without stringly-typed code in controllers

## Notes / decisions

- Repositories stay thin; business rules live in services.
- Returning `ConnectionIO` from repositories is what makes transactional snapshot saves clean later (PFT-018).

## Implementation hints

- Use `doobie-hikari` with the same pool settings as Play's datasource, or let Doobie own the pool entirely — pick one, don't run two pools by accident.
- Turn on Doobie's query type-checking in tests to catch SQL drift after migrations.
