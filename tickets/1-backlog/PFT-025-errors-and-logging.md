# PFT-025 — Error pages, empty states and logging

**Epic:** EPIC-06 Hardening and deploy
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-011

## Purpose

Add not-found and error pages, consistent empty states, structured logging of unexpected failures, and a clear separation between validation errors and system errors.

## Why it matters

A private financial app that shows a raw stack trace is both unpleasant and a small information leak. Logging is what makes problems diagnosable once the app is hosted rather than running in a terminal.

## Tasks

- [ ] Custom 404 page inside the app layout
- [ ] Custom 500 / generic error page that never exposes stack traces in production
- [ ] Custom error handler logging unexpected exceptions with a correlation id shown to the user
- [ ] Ensure validation failures re-render forms rather than hitting the error handler
- [ ] Audit every list page for a proper empty state
- [ ] Configure log levels; make sure no passwords, tokens or full balances are logged

## Acceptance criteria

- [ ] An unknown URL shows the styled 404
- [ ] A forced exception shows a friendly error page in production mode and logs the detail server-side
- [ ] Form validation errors never reach the error page
- [ ] Logs contain no credentials or reset tokens

## Notes / decisions

- Distinguishing validation from system errors keeps the logs meaningful.

## Implementation hints

- Extend Play's `HttpErrorHandler`. Show a short random correlation id on the error page and log it alongside the exception.
