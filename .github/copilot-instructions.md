# Copilot Instructions

## Purpose
This repository is a personal Scala backend project focused on a hosted web application for recording dated personal-finance account balance snapshots over time.

The product priority is a coherent, finishable v1 for snapshot tracking rather than a broad personal-finance platform.

## Stack
- Scala 3
- Play Framework
- Server-rendered Twirl templates
- PostgreSQL
- Doobie
- ScalaTest
- Simple session-based authentication

## Architecture rules
- Keep this as a server-rendered Play monolith for v1.
- Do not introduce a separate frontend/backend split.
- Do not introduce microservices, event-driven architecture, or other distributed patterns unless explicitly requested.
- Prefer simple, explicit, testable designs over abstraction-heavy solutions.
- Prefer explicit SQL-oriented persistence and transactions.
- Do not introduce an ORM or additional persistence framework unless explicitly requested.

## Domain rules
- A snapshot is date-based.
- A snapshot must be trustworthy as a historical record.
- New snapshots should be complete for all active accounts unless explicitly designing an approved exception flow.
- Pre-fill from the previous snapshot is part of the intended create flow.
- Accounts can be archived, not deleted, so historical records remain valid.
- Historical data must remain intact even when account visibility changes later.
- Editing past entries is allowed, but silent history changes are undesirable; preserve last-edited metadata where relevant.

## Product scope rules
- Keep scope centred on account balance snapshots, account lifecycle, and useful historical reporting.
- Do not add budgeting, recurring bills, goal pots, category systems, or broad fintech features unless explicitly requested.
- Prefer small, credible v1 decisions over speculative future-platform design.
- Design for private single-user use first, while keeping the model compatible with future multi-user support.

## Change rules
- Before making multi-file or structural changes, propose a short plan.
- Do not change schema, routes, auth behaviour, or public workflows unless the task requires it.
- Preserve existing naming and error-handling conventions where possible.
- Avoid broad refactors unless there is a clear payoff and the task calls for it.

## Implementation preferences
- Favour service and repository changes that are easy to reason about and test.
- Keep validation explicit.
- Think carefully about transactional boundaries and historical integrity.
- When changing persistence logic, consider duplicate snapshot prevention, archived account behaviour, and completeness of stored data.
- Prefer incremental changes that keep diffs reviewable.

## Testing expectations
- Include or update tests for meaningful behavioural changes.
- Cover happy path, validation failure, and important edge cases.
- For snapshot behaviour, test historical integrity and account archive interactions where relevant.
- Do not claim a change is complete without identifying how it should be validated.

## Response style
- Be concise and practical.
- Explain trade-offs briefly when they matter.
- Flag weak assumptions, contradictions, or missing edge cases clearly.
- Do not widen scope casually.
- If requirements are unclear, ask focused questions rather than guessing.
