# PFT-016 — Snapshot entry action area: + today and date selector

**Epic:** EPIC-04 Snapshot workflow
**Status:** backlog
**Size:** S
**Prerequisites:** PFT-002, PFT-011

## Purpose

Add the persistent top action area with a `+ today` button and an adjacent date selector, both routing into the same snapshot creation flow.

## Why it matters

This is the app's primary action and the fastest path for the common case. Making the everyday path one click while still allowing a back-dated entry is an explicitly agreed UX decision.

## Tasks

- [ ] Add the action area to the dashboard header (and/or main nav)
- [ ] `+ today` links to `/snapshots/new?date=<today>`
- [ ] Adjacent date input with a Go action linking to `/snapshots/new?date=<chosen>`
- [ ] Both routes hit the same controller action and template
- [ ] If a snapshot already exists for that date, redirect to its edit page with an explanatory flash
- [ ] Reject future dates beyond today (or allow deliberately — decide and document)

## Acceptance criteria

- [ ] `+ today` opens the create form pre-set to today's date in one click
- [ ] Choosing a past date opens the same form for that date
- [ ] Picking a date that already has a snapshot goes to edit, not to a duplicate-key error
- [ ] Invalid or malformed date parameters fail gracefully

## Notes / decisions

- Two connected controls, one flow — explicitly agreed.
- One snapshot per user per date is enforced in the database (PFT-005); this is the friendly UI handling of that rule.

## Implementation hints

- Native `<input type="date">` is enough; no date picker library.
- Resolve "today" in the user's timezone (Europe/London), not UTC, or a late-evening snapshot lands on the wrong day.
