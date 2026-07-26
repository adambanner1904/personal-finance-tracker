# PFT-024 — Latest balances by account

**Epic:** EPIC-05 Reporting
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-021

## Purpose

A table of the latest known balance for every account, with its change since the previous snapshot.

## Why it matters

This is the detailed view behind the headline numbers and the quickest way to spot an account that moved unexpectedly or was mis-entered.

## Tasks

- [ ] Query the latest known balance per account from the latest snapshot
- [ ] Include the previous snapshot's balance and the per-account delta
- [ ] Render grouped by institution with institution subtotals
- [ ] Show account type and category on each row
- [ ] Decide and implement how archived accounts appear: excluded from the main table, optionally shown in a separate collapsed section with their last recorded balance
- [ ] Empty state when there are no snapshots

## Acceptance criteria

- [ ] Every active account appears with its latest balance
- [ ] Institution subtotals and the grand total match the dashboard total
- [ ] Per-account deltas are correctly signed
- [ ] Archived account handling matches the documented decision and does not distort totals

## Notes / decisions

- Open decision to close during build: archived accounts in a separate section is the recommended default, since including them in "latest balances" implies they are still current.

## Implementation hints

- `DISTINCT ON (account_id) ... ORDER BY snapshot_date DESC` is the natural Postgres shape if you ever need last-known rather than latest-snapshot balances.
