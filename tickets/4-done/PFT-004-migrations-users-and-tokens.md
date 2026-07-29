# PFT-004 — Migrations: users and password_reset_tokens

**Epic:** EPIC-01 Foundations
**Status:** backlog
**Size:** S
**Prerequisites:** PFT-003

## Purpose

Create the schema for user accounts and password reset tokens.

## Why it matters

Auth is the first real feature and everything else hangs off `user_id`. Reset tokens are security-sensitive, so the table must store a hashed token, an expiry, and one-time-use tracking.

## Tasks

- [x] `V1__create_users.sql`: `id`, `email` (unique, citext or lower-cased), `password_hash`, `created_at`, `updated_at`
- [x] `V2__create_password_reset_tokens.sql`: `id`, `user_id` FK, `token_hash`, `expires_at`, `used_at` nullable, `created_at`
- [x] Unique index on email; index on `password_reset_tokens.user_id`
- [x] FK from tokens to users with `ON DELETE CASCADE`

## Acceptance criteria

- [ ] Migrations apply cleanly from empty
- [ ] Duplicate email insert is rejected by the database, not just the app
- [ ] Raw reset tokens are never stored — only a hash column exists

## Notes / decisions

- Email uniqueness is a hard constraint.
- Store emails normalised (lower-case) to avoid duplicate-by-case accounts.

## Implementation hints

- `bigserial` is fine and simpler than UUID for a single-user app; be consistent across all tables.
- Use `timestamptz` throughout rather than naive timestamps.
