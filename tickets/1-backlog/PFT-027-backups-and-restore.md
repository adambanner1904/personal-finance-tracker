# PFT-027 — Database backup approach and tested restore

**Epic:** EPIC-06 Hardening and deploy
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-026

## Purpose

Decide and implement a backup approach for the production database, write down the restore procedure, and prove it works at least once.

## Why it matters

This app is the single source of truth for years of financial history. An untested backup is not a backup, and losing the snapshot history would be unrecoverable.

## Tasks

- [ ] Decide the approach: managed provider automated backups, scheduled `pg_dump` to object storage, or both
- [ ] Configure the chosen mechanism with a defined retention window
- [ ] Verify backups are actually being produced on schedule
- [ ] Write restore steps in the README: fetch backup, provision empty DB, restore, verify, repoint the app
- [ ] Perform a real restore into a scratch database and confirm the data matches
- [ ] Note the recovery point objective you are accepting (e.g. up to 24 hours of loss)

## Acceptance criteria

- [ ] Backups exist and are visible/verifiable
- [ ] Restore steps are written and were followed successfully at least once
- [ ] A restored database passes a spot check: snapshot count, latest total, entry counts
- [ ] The accepted data-loss window is documented

## Notes / decisions

- Since snapshots are entered infrequently, a daily backup is comfortably good enough.

## Implementation hints

- `pg_dump -Fc` produces a compact custom-format dump restorable with `pg_restore`.
- Test the restore into a throwaway database, never over production.
