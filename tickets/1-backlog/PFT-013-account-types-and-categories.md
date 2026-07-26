# PFT-013 — Model fixed account types and reporting categories

**Epic:** EPIC-03 Institutions and accounts
**Status:** backlog
**Size:** S
**Prerequisites:** PFT-006

## Purpose

Define the fixed v1 account types and reporting categories as proper Scala ADTs with database mappings and display labels.

## Why it matters

These two enums drive the account form, validation, and all category reporting. Modelling them once as types — rather than loose strings — removes a whole class of bugs and keeps the reporting layer honest.

## Tasks

- [ ] `AccountType` ADT: CurrentAccount, SavingsAccount, CashIsa, HouseIsa, InvestmentIsa, InvestmentAccount, Pension
- [ ] `Category` ADT: SpendingMoney, FreeLiquidCapital, LongTermSavings
- [ ] Each with a stable DB value, a human display label, and a `values` list for select boxes
- [ ] Doobie `Meta` instances for both
- [ ] Play form `Formatter`/mapping for both, rejecting unknown values
- [ ] Unit tests covering round-trip and unknown-value rejection

## Acceptance criteria

- [ ] Only the seven account types and three categories are accepted anywhere
- [ ] Adding a select box to a form needs no duplicated string list
- [ ] An unknown value from a tampered form POST is a validation error, not an exception
- [ ] DB values match the CHECK constraints from PFT-005

## Notes / decisions

- These are intentionally fixed for v1, but stored as data on the account so a later version can make them configurable.
- Do not add custom categories in v1.

## Implementation hints

- Scala 3 `enum` or Scala 2 sealed trait + case objects.
- Keep display labels in the ADT, not in Twirl, so reporting and forms agree.
