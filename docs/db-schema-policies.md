# Schema Policies

## Ownership

- An institution can belong to exactly one user
- An account can belong to one institution and by extension one user
- A snapshot belongs to one user
- A snapshot entry belongs to a single snapshot and reference an account owned by the same user

## Uniqueness

- A user can only have one institution with a given name 
- A user can only have one snapshot per date
- A snapshot can only have one entry per an account

## Lifecycle

- Institutions and accounts can be archived meaning they will not be used for future snapshots but will exist historically
- Snapshots are not archived; they can be edited or deleted
- Users are deactivated in preference to hard deletion

## Deletion

- Deleting a snapshot deletes it's entries
- Deleting an account with history is not allowed; accounts with entries must be archived instead

## Timestamps

- created_at is set via default and never changed
- updated_at is maintained via triggers on all main tables