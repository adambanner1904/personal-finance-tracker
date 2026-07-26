# PFT-017 — Snapshot create form with prefill from previous snapshot

**Epic:** EPIC-04 Snapshot workflow
**Status:** backlog
**Size:** L
**Prerequisites:** PFT-015, PFT-016

## Purpose

Build the snapshot entry form: all active accounts grouped by institution, balances prefilled from the most recent snapshot before the chosen date, with completeness validation.

## Why it matters

This is the core workflow of the entire product. Prefill is what turns a tedious full re-entry into 'change the few values that changed', and the completeness rule is what makes every total and delta trustworthy.

## Tasks

- [ ] Query all active accounts for the user, ordered by institution then account name
- [ ] Query the most recent snapshot strictly before the chosen date and load its entries
- [ ] Prefill each account's balance from that snapshot where an entry exists
- [ ] Accounts with no previous entry (new accounts) render blank and are highlighted as new
- [ ] With no previous snapshot at all, render every field blank
- [ ] Group the form visually by institution with a per-institution subtotal
- [ ] Require a value for every active account before save; block submit otherwise
- [ ] Inline per-field validation errors, preserving entered values on re-render
- [ ] Show a live or on-save total, and the delta versus the prefill source
- [ ] Optional snapshot-level notes field

## Acceptance criteria

- [ ] Opening the form for today prefills from the latest earlier snapshot
- [ ] Back-dating to an earlier date prefills from the snapshot before that date, not the latest overall
- [ ] Leaving any active account blank prevents save and shows a clear error on that field
- [ ] Archived accounts do not appear
- [ ] A newly created account appears blank and marked as new
- [ ] Non-numeric or negative-where-invalid input is rejected with a helpful message

## Notes / decisions

- Prefill source is the most recent snapshot *before the chosen date* — this matters for back-dated entries.
- Every active account must have a balance before save. This was explicitly decided.
- Blank-vs-zero for brand new accounts: prefer blank so the user makes a conscious choice, rather than silently recording zero.

## Implementation hints

- Play's `repeatedMapping` / indexed form fields, or a map keyed by account id, handle the dynamic field set.
- Parse money as `BigDecimal`, scale 2. Strip currency symbols and thousands separators before parsing.
- Prefill logic belongs in a `SnapshotService`, not in the controller or the template.
