# PFT-019 — Snapshot history list

**Epic:** EPIC-04 Snapshot workflow
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-018

## Purpose

A list of all snapshots, newest first, with the headline numbers and a link into each one.

## Why it matters

This is how the user navigates their own financial history and the natural landing point after saving. It also makes the value of the app visible at a glance.

## Tasks

- [ ] `GET /snapshots` listing snapshots for the user ordered by `snapshot_date` descending
- [ ] Each row: date, total assets, change versus the previous snapshot, account count, last-edited marker
- [ ] Link each row to the snapshot detail page
- [ ] Empty state prompting the first snapshot
- [ ] Compute totals and row-level deltas in SQL rather than N+1 queries

## Acceptance criteria

- [ ] All snapshots appear newest first with correct totals
- [ ] The delta column matches the difference to the chronologically previous snapshot
- [ ] The oldest snapshot shows no delta rather than a misleading zero
- [ ] The page issues a small, bounded number of queries regardless of history length

## Notes / decisions

- Deltas here follow the same definition as the dashboard: change since the previous snapshot.

## Implementation hints

- A single query with `SUM(balance)` grouped by snapshot plus a window function (`LAG`) gives totals and deltas in one pass.
