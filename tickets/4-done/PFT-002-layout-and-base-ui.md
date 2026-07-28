# PFT-002 — Base Twirl layout, navigation shell, flash messages and base styling

**Epic:** EPIC-01 Foundations
**Status:** done
**Size:** S
**Prerequisites:** PFT-001

## Purpose

Build the shared UI shell every page renders inside: layout template, header/nav, flash message component, and a small stylesheet for forms, tables, buttons and alerts.

## Why it matters

Every later screen depends on this. Doing it once up front keeps the app looking coherent and stops per-page CSS sprawl. The product should feel clean and practical, not overdesigned.

## Tasks

- [x] Create `views/layout/main.scala.html` taking title and content block
- [x] Add header/nav shell (Dashboard, Snapshots, Accounts, Institutions, Logout) with a hook to hide nav when logged out
- [x] Add `views/components/flash.scala.html` rendering `success` / `error` flash keys
- [x] Add `public/stylesheets/main.css` with styles for forms, labels, inputs, validation errors, tables, buttons, alerts, cards
- [ ] Add a simple empty-state partial for reuse on list pages

## Acceptance criteria

- [x] Any page can be rendered by passing a title and body to the layout
- [x] Setting a flash message on redirect displays a visible banner on the next page
- [x] Forms, tables and buttons look consistent without page-specific CSS
- [x] Layout is readable on a laptop and usable on a phone

## Notes / decisions

- Keep CSS hand-written and small; no framework needed for v1.
- Nav is a shell only — links may 404 until their tickets land.

## Implementation hints

- Use a single stylesheet rather than per-page assets.
- Consider a `@authenticatedLayout` variant later rather than branching heavily inside one template.
