# PFT-018 — Transactional snapshot persistence

**Epic:** EPIC-04 Snapshot workflow
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-017, PFT-006

## Purpose

Persist the snapshot header and all its entries atomically, honouring the one-snapshot-per-date and one-entry-per-account constraints.

## Why it matters

A half-written snapshot would corrupt every total and delta downstream. Atomicity plus database constraints is what makes the history reliable.

## Tasks

- [ ] Insert the snapshot header row and return its id
- [ ] Batch insert one entry per submitted account balance
- [ ] Run header and entries in a single transaction
- [ ] Handle the `(user_id, snapshot_date)` unique violation as a friendly form error, not a 500
- [ ] Handle the `(snapshot_id, account_id)` unique violation defensively
- [ ] Redirect to the snapshot detail page on success with a flash

## Acceptance criteria

- [ ] Saving creates exactly one snapshot and one entry per active account
- [ ] An induced failure mid-save leaves no snapshot and no entries behind
- [ ] Saving a duplicate date shows a clear message and offers the edit page
- [ ] Balances round-trip exactly with two decimal places

## Notes / decisions

- Repositories return `ConnectionIO`; the service composes them and runs one `.transact`.

## Implementation hints

- Doobie `Update[...].updateMany` for the entry rows.
- Catch `PSQLException` with SQLSTATE 23505 and map to a form error by constraint name.
