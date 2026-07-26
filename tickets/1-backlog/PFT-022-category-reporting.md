# PFT-022 — Change since previous snapshot by category

**Epic:** EPIC-05 Reporting
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-021

## Purpose

Break the latest-versus-previous comparison down by the three reporting categories.

## Why it matters

The overall number says whether things moved; the category split says why. Spending money falling while long-term savings rises is a very different story from the reverse, and the totals alone hide it.

## Tasks

- [ ] Query latest snapshot totals grouped by account category
- [ ] Query previous snapshot totals grouped by category
- [ ] Calculate per-category deltas, including categories present in only one of the two
- [ ] Render category cards or a compact summary table with current value, change and share of total
- [ ] Use the display labels from the Category ADT

## Acceptance criteria

- [ ] Category totals sum exactly to the overall total
- [ ] A category with no accounts renders as zero or is omitted deliberately, not as an error
- [ ] Deltas are correctly signed per category
- [ ] Category labels match those used on the account form

## Notes / decisions

- Categories are fixed for v1: spending money, free/liquid capital, long-term savings.
- Grouping reads the category stored on the account, so this stays correct if categories become configurable later.

## Implementation hints

- A single query joining `snapshot_entries` to `accounts` and grouping by `(snapshot_id, category)` covers both snapshots at once.
