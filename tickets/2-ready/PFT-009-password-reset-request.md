# PFT-009 — Password reset request and token issue

**Epic:** EPIC-02 Authentication
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-004, PFT-008

## Purpose

Let a user request a reset by email, generating a single-use, expiring token stored only as a hash.

## Why it matters

Without reset, a forgotten password means losing access to years of financial history. Tokens are a classic weak point, so hashing and expiry are non-negotiable.

## Tasks

- [ ] `GET /reset` request form and `POST /reset`
- [ ] Generate a cryptographically random token; store only its hash with an expiry (e.g. 1 hour)
- [ ] Invalidate any previous unused tokens for that user
- [ ] Deliver the reset link (v1 may log the link locally; document the production email path)
- [ ] Always show the same "if that email exists, a reset link has been sent" confirmation

## Acceptance criteria

- [ ] Requesting a reset creates a row with a hashed token and a future expiry
- [ ] The raw token exists only in the link, never in the database
- [ ] Unknown emails produce the identical confirmation screen
- [ ] Requesting twice invalidates the first token

## Notes / decisions

- Email delivery mechanism is a v1 decision to make at build time; logging the link locally is acceptable for a private single-user launch as long as production has a real path.

## Implementation hints

- `SecureRandom` 32 bytes, URL-safe base64, SHA-256 the token for storage.
- Link shape: `/reset/complete?token=...`.
