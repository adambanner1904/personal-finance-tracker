# Personal Finance Tracker — Ticket Board

A lightweight file-based board. No Jira, no ceremony. Move a ticket file between folders as it progresses.

```
tickets/
  backlog/      not started
  ready/        next up, prerequisites met, ready to pick up
  in-progress/  actively being built (keep to ONE ticket)
  done/         complete per the definition of done
```

## How to use it

1. Work top to bottom by ticket number — the order matches the agreed build order.
2. Pull the next ticket into `ready/`, then into `in-progress/` when you start it.
3. Keep exactly one ticket in `in-progress/`.
4. Update the `**Status:**` line in the file when you move it.
5. Tick tasks off inside the ticket as you go; add notes under the tasks rather than breaking flow.
6. Move to `done/` only when the definition of done is met.

## Definition of done

- Works locally end to end
- Validation is present
- Happy path manually tested
- Obvious failure cases handled
- Committed with a sensible message

## Ticket index

### EPIC-01 Foundations
| ID | Title | Size |
|---|---|---|
| PFT-001 | Play Scala project skeleton and package structure | S |
| PFT-002 | Base Twirl layout, navigation shell, flash messages and base styling | S |
| PFT-003 | PostgreSQL connection and Flyway migration setup | S |
| PFT-004 | Migrations: users and password_reset_tokens | S |
| PFT-005 | Migrations: institutions, accounts, snapshots, snapshot_entries | M |
| PFT-006 | Doobie transactor, repository package and query conventions | M |

### EPIC-02 Authentication
| ID | Title | Size |
|---|---|---|
| PFT-007 | Sign up with hashed password and session start | M |
| PFT-008 | Login and logout | S |
| PFT-009 | Password reset request and token issue | M |
| PFT-010 | Password reset completion | M |
| PFT-011 | Authenticated action builder, route protection, CSRF and ownership checks | M |

### EPIC-03 Institutions and accounts
| ID | Title | Size |
|---|---|---|
| PFT-012 | Institutions list, create and edit | M |
| PFT-013 | Model fixed account types and reporting categories | S |
| PFT-014 | Accounts list, create and edit | M |
| PFT-015 | Account archive behaviour | M |

### EPIC-04 Snapshot workflow
| ID | Title | Size |
|---|---|---|
| PFT-016 | Snapshot entry action area: + today and date selector | S |
| PFT-017 | Snapshot create form with prefill from previous snapshot | L |
| PFT-018 | Transactional snapshot persistence | M |
| PFT-019 | Snapshot history list | M |
| PFT-020 | Snapshot detail and edit with last-edited timestamp | L |

### EPIC-05 Reporting
| ID | Title | Size |
|---|---|---|
| PFT-021 | Dashboard summary: total assets and change since previous snapshot | M |
| PFT-022 | Change since previous snapshot by category | M |
| PFT-023 | Trend charts: total assets, free/liquid capital and long-term savings | M |
| PFT-024 | Latest balances by account | M |

### EPIC-06 Hardening and deploy
| ID | Title | Size |
|---|---|---|
| PFT-025 | Error pages, empty states and logging | M |
| PFT-026 | Externalise configuration and deploy the first working version | L |
| PFT-027 | Database backup approach and tested restore | M |

## Guardrails carried over from the project context

- v1 is an authenticated **snapshot tracker**, not a budgeting or transaction app.
- Stack is fixed: Scala, Play, Twirl, Doobie, PostgreSQL, Flyway, Chart.js.
- Server-rendered single app. No frontend/backend split in v1.
- Archive behaviour is a first-class feature.
- Change since the previous snapshot is the primary dashboard metric.
- Account types and categories are fixed for v1, but stored as data on the account.
- Out of scope: debts, budgeting, custom categories, pots, open banking, multi-user, over-engineering.
