# PFT-010 — Password reset completion

**Epic:** EPIC-02 Authentication
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-009

## Purpose

Accept a valid reset token, let the user set a new password, and mark the token used.

## Why it matters

This closes the reset loop. The validation rules here — exists, not expired, not already used — are what make the feature safe.

## Tasks

- [ ] `GET /reset/complete?token=...` renders a new-password form (or an invalid-token page)
- [ ] `POST /reset/complete` validates token hash, expiry, and unused state
- [ ] Apply the same password strength rules as sign up, with confirmation field
- [ ] Save the new password hash and set `used_at` in one transaction
- [ ] Redirect to login with a success flash (do not auto-login)

## Acceptance criteria

- [ ] A valid token allows a password change and the new password works
- [ ] An expired, unknown, or already-used token shows a clear invalid-link page
- [ ] The token cannot be reused after success
- [ ] Any existing session behaviour after reset is deliberate and documented

## Notes / decisions

- Not auto-logging-in after reset keeps the flow simple and verifies the new password works.

## Implementation hints

- Do the password update and `used_at` set in a single `ConnectionIO` transaction so you can never end with one applied.
