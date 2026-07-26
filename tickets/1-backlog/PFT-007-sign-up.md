# PFT-007 — Sign up with hashed password and session start

**Epic:** EPIC-02 Authentication
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-004, PFT-006, PFT-002

## Purpose

Let a user create an account with email and password, hashing the password before storage and starting a session on success.

## Why it matters

The app holds personal financial data on a hosted URL, so it cannot be open. Sign up is the entry point to everything else.

## Tasks

- [ ] `GET /signup` renders the form; `POST /signup` handles it
- [ ] Play form mapping: email (valid format, normalised), password (min length), password confirmation
- [ ] Hash the password with a modern algorithm before persisting
- [ ] Insert the user and start a session
- [ ] Catch the unique-email violation and show a friendly field error rather than a stack trace
- [ ] Redirect to the dashboard on success with a flash message

## Acceptance criteria

- [ ] A new email creates a user and lands logged in on the dashboard
- [ ] A duplicate email shows "an account with this email already exists" on the form
- [ ] Invalid email or short password re-renders the form with inline errors and no data loss
- [ ] The plaintext password never appears in the database or logs

## Notes / decisions

- Single-user app at launch, but sign up is still built properly so multi-user is possible later.

## Implementation hints

- Use BCrypt (e.g. `jbcrypt`) or Argon2; do not hand-roll hashing.
- Normalise email to lower-case in the form mapping so the DB unique index is meaningful.
