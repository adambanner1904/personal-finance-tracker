# PFT-005 — Migrations: institutions, accounts, snapshots, snapshot_entries

**Epic:** EPIC-01 Foundations
**Status:** backlog
**Size:** M
**Prerequisites:** PFT-004

## Purpose

Create the core domain schema with all foreign keys and the two business-critical unique constraints.

## Why it matters

The whole product is this shape. Getting the constraints in place at the database level protects the integrity of totals and delta calculations, which is the point of the app.

## Tasks

- [ ] `institutions`: `id`, `user_id` FK, `name`, `created_at`, `updated_at`
- [ ] `accounts`: `id`, `user_id` FK, `institution_id` FK, `name`, `account_type`, `category`, `archived_at` nullable, `created_at`, `updated_at`
- [ ] `snapshots`: `id`, `user_id` FK, `snapshot_date` date, `notes` nullable, `created_at`, `updated_at`, `last_edited_at` nullable
- [ ] `snapshot_entries`: `id`, `snapshot_id` FK, `account_id` FK, `balance numeric(14,2)`, `created_at`, `updated_at`
- [ ] Unique `(user_id, snapshot_date)` on snapshots
- [ ] Unique `(snapshot_id, account_id)` on snapshot_entries
- [ ] CHECK constraints (or a lookup table) restricting `account_type` and `category` to the fixed v1 values
- [ ] Indexes for the common reads: `accounts(user_id, archived_at)`, `snapshots(user_id, snapshot_date desc)`, `snapshot_entries(snapshot_id)`

## Acceptance criteria

- [ ] All migrations apply from empty in order
- [ ] Two snapshots for the same user and date are rejected
- [ ] Two entries for the same account in one snapshot are rejected
- [ ] An invalid account_type or category is rejected at the DB level

## Notes / decisions

- Fixed v1 account types: current account, savings account, cash ISA, house ISA, investment ISA, investment account, pension.
- Fixed v1 categories: spending money, free/liquid capital, long-term savings.
- Category is stored on the account as data (not hard-wired in reporting code) so it can become configurable later.
- Accounts are archived, never hard-deleted, so no cascade delete on accounts.

## Implementation hints

- Store enum values as snake_case text (`current_account`, `free_liquid_capital`) and map to a Scala ADT.
- `numeric(14,2)` for money — never floating point.
