# PFT-020 — Snapshot detail and edit with last-edited timestamp

**Epic:** EPIC-04 Snapshot workflow
**Status:** backlog
**Size:** L
**Prerequisites:** PFT-018

## Purpose

View a saved snapshot in full and edit its balances, recording and displaying a visible last-edited timestamp.

## Why it matters

Mistakes happen and corrections must be possible, but silently rewriting financial history destroys trust in the numbers. The visible last-edited marker is the agreed compromise.

## Tasks

- [ ] `GET /snapshots/:id` detail page: date, total, per-institution grouping, per-account balances
- [ ] Show entries for archived accounts that were active at the time, clearly marked
- [ ] Show change versus the previous snapshot per account
- [ ] Edit mode loading saved values into the same form component used for create
- [ ] `POST /snapshots/:id` updates entries transactionally and sets `last_edited_at`
- [ ] Display "last edited <timestamp>" prominently whenever it is set
- [ ] Decide and implement behaviour for accounts created after the snapshot date (default: not added retroactively)

## Acceptance criteria

- [ ] A saved snapshot renders every entry including archived accounts
- [ ] Editing a balance and saving persists the change and sets the timestamp
- [ ] The last-edited marker is visible on both the detail page and the history list
- [ ] An unedited snapshot shows no last-edited marker
- [ ] Ownership is enforced: another user's snapshot id returns not-found

## Notes / decisions

- Editable snapshots with a visible last-edited timestamp was the explicitly chosen option.
- No full audit trail or version history in v1 — just the timestamp.

## Implementation hints

- Reuse the create form component so validation rules cannot drift between create and edit.
- Update entries with an upsert on `(snapshot_id, account_id)` inside one transaction.
