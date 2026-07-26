# PFT-023 — Trend charts: total assets, free/liquid capital and long-term savings over time

**Epic:** EPIC-05 Reporting
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-022

## Purpose

Three time-series line charts over snapshot dates, rendered with a lightweight chart library.

## Why it matters

Total assets over time is the agreed secondary reporting goal. The two category trends show whether liquidity and long-term position are moving in the intended direction.

## Tasks

- [ ] Query total assets per snapshot date
- [ ] Query free/liquid capital per snapshot date
- [ ] Query long-term savings per snapshot date
- [ ] Serialise series to JSON for the templates
- [ ] Render three Chart.js line charts with currency-formatted axes and date labels
- [ ] Handle fewer than two data points gracefully (show the value, hide the chart)
- [ ] Keep charts below the summary cards on the dashboard

## Acceptance criteria

- [ ] Each chart plots one point per snapshot in date order
- [ ] Chart values match the summary card figures for the latest date
- [ ] With zero or one snapshot the page renders cleanly with no broken canvas
- [ ] Charts are readable on a laptop screen

## Notes / decisions

- Chart.js is the agreed library. Keep it simple — no dashboard framework.
- Reporting stays anchored on change since previous snapshot; charts are supporting context, not the headline.

## Implementation hints

- One query with conditional aggregation can produce all three series in a single pass over snapshot dates.
- Pass data via a JSON script block rather than building charts from templated JS strings.
