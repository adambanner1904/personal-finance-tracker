# PFT-011 — Authenticated action builder, route protection, CSRF and ownership checks

**Epic:** EPIC-02 Authentication
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-008

## Purpose

Add the cross-cutting security layer: an authenticated action that resolves the current user, protection on every non-auth route, CSRF on all forms, secure cookie settings, and per-user ownership checks on all data access.

## Why it matters

This is the single most important ticket for keeping financial data safe. Doing it now — before institutions, accounts and snapshots exist — means every later feature is secure by default instead of retrofitted.

## Tasks

- [x] Build an `AuthenticatedAction` action builder producing a request with the resolved user
- [x] Apply it to every route except signup, login, reset request and reset complete
- [x] Unauthenticated requests redirect to login (remember the intended URL if easy)
- [x] Enable Play's CSRF filter and add the token to every form
- [x] Configure secure session cookies for production: `httpOnly`, `secure`, `sameSite=Lax`, signed with an env-supplied secret
- [x] Establish the convention that every repository read and write is filtered by `user_id`
- [x] Return 404 (not 403) for another user's resource id

## Acceptance criteria

- [x] Hitting any app URL while logged out redirects to login
- [x] A form POST without a CSRF token is rejected
- [x] Session cookie flags are correct in the production config
- [x] Manually crafting a request for a non-owned record returns not-found, not data

## Notes / decisions

- Per-user isolation matters even though there is one user today, because the model is meant to allow multi-user later.
- 404-over-403 avoids confirming that a record exists.

## Implementation hints

- Prefer `WHERE id = ? AND user_id = ?` in SQL over checking ownership in Scala after loading — one query, no leak.
- Keep the action builder in a `controllers.actions` package.
