# PFT-008 — Login and logout

**Epic:** EPIC-02 Authentication
**Status:** backlog
**Size:** S
**Prerequisites:** PFT-007

## Purpose

Authenticate an existing user against their password hash, start a session, and provide a logout that clears it.

## Why it matters

Day-to-day access depends on this. It also needs to fail safely — no user enumeration, no partial sessions.

## Tasks

- [x] `GET /login` and `POST /login`
- [x] Form mapping with email and password
- [x] Look up user by normalised email and verify the hash
- [x] On success set the session (user id) and redirect to the dashboard
- [x] On failure show a single generic "email or password is incorrect" error
- [x] `POST /logout` clears the session and redirects to login with a flash

## Acceptance criteria

- [x] Valid credentials log in and reach the dashboard
- [x] Wrong password and unknown email produce the same generic error
- [x] Logout clears the session; going back to a protected page redirects to login
- [x] Password verification is constant-time via the hashing library

## Notes / decisions

- Deliberately generic error message to avoid revealing which emails are registered.

## Implementation hints

- Store only the user id in the session cookie; look the user up per request.
- Logout should be a POST with CSRF, not a GET link.
