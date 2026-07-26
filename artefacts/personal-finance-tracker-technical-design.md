# Personal Finance Tracker — Technical Design Note

## Technical direction

The first release should be built as a server-rendered Scala web application using Play Framework and Twirl, backed by PostgreSQL and Doobie. This keeps the project focused on Scala, SQL, domain modelling, and delivery rather than splitting effort across a separate frontend stack too early.[conversation_history:14][conversation_history:15]

## Chosen stack

| Layer | Choice | Rationale |
|---|---|---|
| Language | Scala | Supports the primary learning goal of improving Scala skill.[conversation_history:14] |
| Web framework | Play Framework | Mature Scala web framework with strong support for server-rendered applications.[web:167] |
| Rendering | Twirl | Built into Play and sufficient for forms, tables, and dashboard screens.[web:167] |
| Authentication | Simple session auth | Lower complexity than a full auth framework and enough for v1 email/password plus reset.[conversation_history:15] |
| Database | PostgreSQL | Strong fit for relational data, snapshots, accounts, institutions, and reporting queries.[conversation_history:14][web:166] |
| SQL layer | Doobie | Emphasises explicit SQL and helps build stronger query fluency.[conversation_history:15] |
| Migrations | Flyway | Simple and reliable schema versioning for a small app. |
| Charting | Chart.js | Lightweight choice for time-series charts in a server-rendered UI. |
| Hosting | Small managed host + managed Postgres | Enough for a private hosted v1. |

## Why not a split frontend and backend

A separate frontend and backend architecture would increase coordination, wiring, and deployment complexity without improving the core value of the first release. The application is primarily authenticated CRUD plus reporting, so a server-rendered application is the better trade-off for v1.[conversation_history:15]

## High-level architecture

The suggested application structure is:

- `controllers` — auth, dashboard, institutions, accounts, snapshots
- `services` — business logic such as snapshot prefill, delta calculations, archive rules, and password-reset flows
- `persistence` or `repositories` — Doobie queries and persistence wiring
- `models.domain` — domain entities and enums
- `forms` — Play form mappings and validation rules
- `views` — Twirl templates
- `db/migration` — Flyway SQL migrations

The main architectural rule should be: keep SQL explicit, keep repositories thin, and put business rules in services. This makes the code easier to reason about and easier to explain as design work rather than accidental coupling.

## Initial schema

### `users`

| Column | Type | Notes |
|---|---|---|
| `id` | UUID / bigserial | Primary key |
| `email` | text | Unique, indexed |
| `password_hash` | text | Hashed password only |
| `created_at` | timestamp | Audit |
| `updated_at` | timestamp | Audit |

### `password_reset_tokens`

| Column | Type | Notes |
|---|---|---|
| `id` | UUID / bigserial | Primary key |
| `user_id` | FK -> users | Owner |
| `token_hash` | text | Store hashed token rather than raw token |
| `expires_at` | timestamp | Reset expiry |
| `used_at` | timestamp nullable | One-time use tracking |
| `created_at` | timestamp | Audit |

### `institutions`

| Column | Type | Notes |
|---|---|---|
| `id` | UUID / bigserial | Primary key |
| `user_id` | FK -> users | Ownership |
| `name` | text | Provider name, for example Halifax |
| `created_at` | timestamp | Audit |
| `updated_at` | timestamp | Audit |

### `accounts`

| Column | Type | Notes |
|---|---|---|
| `id` | UUID / bigserial | Primary key |
| `user_id` | FK -> users | Ownership |
| `institution_id` | FK -> institutions | Grouping |
| `name` | text | Account display name |
| `account_type` | text / enum | Current account, savings account, cash ISA, house ISA, investment ISA, investment account, pension.[conversation_history:8] |
| `category` | text / enum | Spending money, free/liquid capital, long-term savings.[conversation_history:9] |
| `archived_at` | timestamp nullable | Null means active |
| `created_at` | timestamp | Audit |
| `updated_at` | timestamp | Audit |

### `snapshots`

| Column | Type | Notes |
|---|---|---|
| `id` | UUID / bigserial | Primary key |
| `user_id` | FK -> users | Ownership |
| `snapshot_date` | date | One financial picture for a date |
| `notes` | text nullable | Optional snapshot-level note |
| `created_at` | timestamp | Audit |
| `updated_at` | timestamp | Audit |
| `last_edited_at` | timestamp nullable | Visible edit history marker |

A unique constraint on `(user_id, snapshot_date)` is recommended unless there is a strong reason to allow multiple snapshots per day.

### `snapshot_entries`

| Column | Type | Notes |
|---|---|---|
| `id` | UUID / bigserial | Primary key |
| `snapshot_id` | FK -> snapshots | Parent snapshot |
| `account_id` | FK -> accounts | Account for the balance |
| `balance` | numeric(14,2) | Stored monetary value |
| `created_at` | timestamp | Audit |
| `updated_at` | timestamp | Audit |

A unique constraint on `(snapshot_id, account_id)` should prevent duplicate entries for the same account within one snapshot.

## Key business rules

### Snapshot creation

- A snapshot is created for either today’s date or a user-selected date.[conversation_history:13]
- The form should prefill from the most recent previous snapshot for active accounts only.[conversation_history:5]
- New accounts that did not exist in the previous snapshot should appear blank or default to zero depending on final UX decision.[conversation_history:5]
- Archived accounts should not appear on new snapshot forms.[conversation_history:5]
- Every active account must have a value before save.[conversation_history:10]

### Snapshot editing

- Saved snapshots remain editable.[conversation_history:12]
- Any update to a snapshot should update `last_edited_at`.[conversation_history:12]
- Editing should preserve the invariant that every active account represented in that snapshot remains complete according to the rules decided for that date.

### Archive behaviour

- Accounts are never hard-deleted in normal use.[conversation_history:5]
- Archiving makes an account invisible for new snapshot entry but preserves all historical entries.[conversation_history:5]

## Query patterns

Doobie should be used to keep the SQL visible and intentional. Expected key queries include:

- Fetch active accounts for a user ordered by institution and account name.
- Fetch latest snapshot for a user.
- Fetch previous snapshot before a chosen date.
- Fetch balances for a given snapshot.
- Compute latest-vs-previous delta overall.
- Compute latest-vs-previous delta grouped by account category.
- Build time-series totals by snapshot date.
- Build time-series totals by category across snapshot dates.

Where practical, reporting queries should be pushed to SQL rather than recomputed inefficiently in memory. However, business meaning such as “delta from previous snapshot” should remain understandable at the service layer rather than becoming opaque.

## UI screens

Suggested v1 screens:

- Login
- Sign up
- Password reset request
- Password reset confirmation
- Dashboard
- Institutions list/create/edit
- Accounts list/create/edit/archive
- New snapshot form
- Snapshot history list
- Snapshot detail/edit page

## Validation and security

The application stores personal financial information, so v1 should include:

- Session-based authentication using secure cookies.[conversation_history:15]
- Password hashing with a modern algorithm.
- CSRF protection on forms.
- Server-side validation for all user input.
- Per-user ownership checks on every read and write.
- HTTPS in production.[web:124][web:126][web:131]
- Database backup plan.[web:134]

Using an established set of security basics is essential even for an MVP, especially when dealing with financial data.[web:124][web:126][web:131][web:134]

## Delivery phases

### Phase 1 — Foundations

- Project setup
- Flyway migrations
- User table and auth flows
- Basic layout and navigation

### Phase 2 — Core setup

- Institution CRUD
- Account CRUD
- Archive behaviour
- Validation rules for account type and category

### Phase 3 — Snapshot workflow

- Snapshot creation action with “+ today” and date selector.[conversation_history:13]
- Prefill logic from previous snapshot.[conversation_history:5]
- Full-form validation requiring all active accounts.[conversation_history:10]
- Snapshot editing and last-edited timestamp.[conversation_history:12]

### Phase 4 — Reporting

- Dashboard tiles
- Latest-vs-previous delta logic
- Category delta reporting
- Total assets and category trend charts

### Phase 5 — Hardening and deploy

- Error states and empty states
- Backups and environment configuration
- Production deployment
- Basic logging and monitoring

## Future extension points

The design should stay open to:

- User-defined categories replacing the fixed enum.[conversation_history:9]
- Liability and debt modelling in a later phase.[conversation_history:3]
- Richer metadata on institutions or accounts.
- Optional frontend enhancement beyond Twirl.
- Eventual separation into frontend and backend only if the product meaningfully outgrows the server-rendered architecture.[conversation_history:15]
