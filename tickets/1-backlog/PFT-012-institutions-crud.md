# PFT-012 — Institutions list, create and edit

**Epic:** EPIC-03 Institutions and accounts
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-011, PFT-005

## Purpose

Full CRUD (minus delete) for institutions — the financial providers such as Halifax, Starling or Trading212 that accounts group under.

## Why it matters

Institutions are the grouping layer that makes multiple accounts per provider possible, which is one of the things the spreadsheet handled badly.

## Tasks

- [ ] Institution repository: list by user, find by id+user, insert, update
- [ ] `GET /institutions` list page showing name and account count
- [ ] `GET/POST /institutions/new` create form
- [ ] `GET/POST /institutions/:id/edit` edit form
- [ ] Validate name: required, trimmed, sensible max length, unique per user
- [ ] Empty state on the list page pointing to "add your first institution"

## Acceptance criteria

- [ ] An institution can be created, appears in the list, and can be renamed
- [ ] Blank or whitespace-only names are rejected with an inline error
- [ ] Another user's institution id returns not-found
- [ ] Renaming an institution does not affect its accounts or historical data

## Notes / decisions

- No delete in v1. If an institution stops being used, its accounts get archived (PFT-015).

## Implementation hints

- Sort the list alphabetically; it doubles as the source for the account form's institution select.
