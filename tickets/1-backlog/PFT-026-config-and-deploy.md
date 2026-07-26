# PFT-026 — Externalise configuration and deploy the first working version

**Epic:** EPIC-06 Hardening and deploy
**Status:** backlog
**Size:** L
**Prerequisites:** PFT-011, PFT-025

## Purpose

Move all secrets and environment-specific values to environment variables, create a production config, verify a production-like start locally, choose a host, and deploy.

## Why it matters

Until it is hosted, this is still a local experiment. Deploying early with real config discipline avoids a scramble later and forces the HTTPS and cookie-security work to be real.

## Tasks

- [ ] Move DB URL, credentials, application secret and base URL to env vars
- [ ] Confirm no secrets are committed anywhere in the repo or its history
- [ ] Create `prod.conf` with secure cookies, `allowedHosts` and appropriate log levels
- [ ] Build a production artefact (`sbt dist` or a Docker image) and run it locally against a real Postgres
- [ ] Choose a host: small managed app host plus managed Postgres
- [ ] Provision production Postgres and run Flyway against it
- [ ] Deploy, verify HTTPS, verify login, create a real snapshot end to end
- [ ] Document the deploy steps in the README

## Acceptance criteria

- [ ] The app starts in production mode with configuration supplied only via env vars
- [ ] Production is served over HTTPS with secure session cookies
- [ ] Migrations run cleanly against the production database
- [ ] A real snapshot can be created and read back on the hosted instance
- [ ] Redeploying is a documented, repeatable sequence

## Notes / decisions

- Hosting choice: a small managed host with managed Postgres is sufficient. Avoid over-engineering the infrastructure.
- Play refuses to start in prod without an application secret — supply it from the environment, never a file in git.

## Implementation hints

- Set `play.filters.hosts.allowed` or every production request returns 400.
- Run Flyway as an explicit deploy step rather than on startup once you are in production.
