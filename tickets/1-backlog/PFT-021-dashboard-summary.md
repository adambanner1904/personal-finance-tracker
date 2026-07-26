# PFT-021 — Dashboard summary: total assets and change since previous snapshot

**Epic:** EPIC-05 Reporting
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-019

## Purpose

The headline dashboard cards: total assets in the latest snapshot and the overall change since the previous one, with clean empty and single-snapshot states.

## Why it matters

Change since the previous snapshot is the primary reporting metric for the whole product. The dashboard should answer 'how has my position changed since I last recorded it?' before it does anything else.

## Tasks

- [ ] Query the latest snapshot for the user
- [ ] Query the immediately previous snapshot
- [ ] Calculate total assets for the latest snapshot
- [ ] Calculate the overall delta, in absolute value and percentage
- [ ] Render summary cards with the snapshot date and up/down styling
- [ ] Empty state when there are no snapshots, pointing at `+ today`
- [ ] Single-snapshot state showing the total with no delta

## Acceptance criteria

- [ ] Totals match the sum of the latest snapshot's entries exactly
- [ ] The delta compares against the chronologically previous snapshot
- [ ] Zero snapshots and one snapshot both render sensibly with no errors or NaN
- [ ] Positive and negative changes are visually distinguishable and correctly signed

## Notes / decisions

- Do not turn the dashboard into a generic analytics screen. It is anchored on the change question.
- Percentage change is undefined when the previous total is zero — handle it rather than dividing by zero.

## Implementation hints

- Delta calculation lives in a `ReportingService` so its meaning stays legible, even though the sums are done in SQL.
