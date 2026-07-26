# PFT-001 — Play Scala project skeleton and package structure

**Epic:** EPIC-01 Foundations
**Status:** backlog
**Size:** S
**Prerequisites:** None

## Purpose

Create the Play Framework Scala project that everything else is built inside, with the agreed package layout and a documented local run.

## Why it matters

Nothing else can start until there is a running app. Getting the package shape right on day one avoids a painful reshuffle later, and the layered split (controllers / services / persistence) is the main architectural rule for this project.

## Tasks

- [ ] Create a Play Scala project (sbt, Scala 2.13 or 3.x, Play 2.9/3.x)
- [ ] Add package structure: `controllers`, `services`, `persistence`, `models.domain`, `forms`, `views`, `conf/db/migration`
- [ ] Add a placeholder `HomeController` and a `GET /` route that renders a page
- [ ] Add `.gitignore`, initialise git, first commit
- [ ] Write README with prerequisites, `sbt run`, and local DB setup steps
- [ ] Add config placeholders in `application.conf` for DB URL, user, password, app secret (env-var overrides)

## Acceptance criteria

- [ ] `sbt run` starts the app and `http://localhost:9000` returns a page
- [ ] Package directories exist and are empty-but-intentional, not ad hoc
- [ ] README lets a cold start work from a clean machine
- [ ] No secrets committed; config reads from env vars with local defaults

## Notes / decisions

- Single server-rendered app. Do not split frontend and backend for v1.
- Architectural rule: SQL explicit, repositories thin, business rules in services.

## Implementation hints

- Use `sbt new playframework/play-scala-seed.g8` as a starting point.
- Config pattern: `db.default.url = ${?DATABASE_URL}` so production can override without code changes.
