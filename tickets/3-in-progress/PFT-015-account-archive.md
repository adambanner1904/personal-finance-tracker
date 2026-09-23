# PFT-015 — Account archive behaviour

**Epic:** EPIC-03 Institutions and accounts
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-014

## Purpose

Allow accounts to be archived (and unarchived) so they drop out of new snapshot forms while all historical data stays intact and visible.

## Why it matters

This is the single behaviour that most directly solves the spreadsheet pain: closing an account previously meant either losing history or leaving dead columns. Archive is a first-class feature, not an afterthought.

## Tasks

- [ ] `POST /accounts/:id/archive` sets `archived_at`
- [ ] `POST /accounts/:id/unarchive` clears it
- [ ] Active-account queries filter on `archived_at IS NULL`
- [ ] Account list keeps showing archived accounts, clearly marked and visually de-emphasised (or in a separate section)
- [ ] Archived accounts are excluded from new snapshot forms
- [ ] Historical snapshots continue to display entries for archived accounts
- [ ] Confirmation step before archiving, with copy explaining history is kept

## Acceptance criteria

- [ ] Archiving removes the account from the next new-snapshot form
- [ ] A past snapshot that included the account still shows its balance
- [ ] The archived account is still visible and labelled on the accounts list
- [ ] Unarchiving brings it back into new snapshot forms with no data loss
- [ ] No hard delete exists anywhere in the UI

## Notes / decisions

- Accounts are never hard-deleted in normal use.
- Archived accounts contribute to historical totals for the dates they were active; they simply stop being required going forward.

## Implementation hints

- One `activeAccounts(userId)` repository method used everywhere avoids someone forgetting the archive filter.
- Reporting queries should read from `snapshot_entries`, not from the current active account list, so archived history survives.
