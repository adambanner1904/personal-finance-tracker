# PFT-014 — Accounts list, create and edit

**Epic:** EPIC-03 Institutions and accounts
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-012, PFT-013

## Purpose

CRUD for accounts, each belonging to an institution and carrying a fixed account type and reporting category.

## Why it matters

Accounts are the real tracked entities. This is what replaces the spreadsheet's hard-coded columns and makes the model extensible when a new account is opened.

## Tasks

- [ ] Account repository: list by user (with institution join), list active, find by id+user, insert, update
- [ ] `GET /accounts` list grouped by institution, showing name, type, category and active/archived state
- [ ] `GET/POST /accounts/new` with selects for institution, account type and category
- [ ] `GET/POST /accounts/:id/edit`
- [ ] Server-side validation: name required, institution must belong to the user, type and category must be valid
- [ ] Empty state prompting institution creation first if none exist

## Acceptance criteria

- [ ] An account can be created under an institution and appears grouped correctly
- [ ] Type and category selects show human labels and persist the right DB values
- [ ] Submitting an institution id owned by someone else is rejected
- [ ] Editing an account never alters historical snapshot entries

## Notes / decisions

- Multiple accounts per institution is a core requirement, not an edge case.
- Changing an account's category changes future and past reporting groupings — accepted for v1, worth a note in the UI.

## Implementation hints

- Order accounts by institution name then account name; the same ordering is reused by the snapshot form.
