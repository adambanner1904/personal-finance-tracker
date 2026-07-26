# Personal Finance Tracker — Product Spec

## Overview

Personal Finance Tracker is a hosted web application for recording dated snapshots of personal financial account balances and tracking how total capital changes over time. The first release is intentionally narrow: authenticated access, institution and account management, complete dated snapshots, and reporting focused on change since the previous snapshot.[conversation_history:2][conversation_history:9][conversation_history:11]

The main problem it solves is the weakness of spreadsheet-based tracking when account structures change. The product preserves historical data when accounts are archived, supports multiple accounts under the same institution, and allows new snapshots to be prefilled from the previous entry so only changed values need editing.[conversation_history:5][conversation_history:6]

## Product goals

The primary goal of v1 is to make financial state tracking quick, accurate, and historically reliable. The key value metric is the change in total capital since the previous snapshot, with particular emphasis on free/liquid capital and long-term savings.[conversation_history:9]

Secondary goals are to provide a clean total-assets-over-time view and a stable base for future extensions such as custom categories, liabilities, and broader multi-user support. Good MVP planning prioritises a small core workflow, essential security, and explicit out-of-scope decisions for the first release.[web:21][web:24][web:124]

## Target user

The initial user is a single authenticated user managing their own personal finances, but the domain model should support future expansion to multiple users later.[conversation_history:2]

## Core workflow

The core workflow for v1 is:

1. Sign in to the application.[conversation_history:11]
2. Create institutions and accounts.[conversation_history:6]
3. Click a top-level snapshot action with two adjacent controls: a “+ today” action for the common case and a date selector for creating a snapshot on a chosen date.[conversation_history:13]
4. Open a full snapshot form containing all active accounts, prefilled from the previous snapshot.[conversation_history:4][conversation_history:5]
5. Enter or confirm a balance for every active account and save the snapshot.[conversation_history:10]
6. View the resulting change since the previous snapshot overall and by fixed category, plus total assets over time.[conversation_history:9]

## Functional scope

### Included in v1

- Hosted authenticated web app.[conversation_history:11]
- Email and password login with password reset.[conversation_history:11]
- Simple session-based authentication.[conversation_history:15]
- Institution management.[conversation_history:6]
- Real financial account management.[conversation_history:6]
- Fixed account types: current account, savings account, cash ISA, house ISA, investment ISA, investment account, pension.[conversation_history:8]
- Fixed v1 account categories: spending money, free/liquid capital, long-term savings.[conversation_history:9]
- Account archiving, where archived accounts disappear from new snapshot forms but remain in history.[conversation_history:5]
- Snapshot creation for today or a chosen date.[conversation_history:13]
- Snapshot prefill from the previous snapshot.[conversation_history:5]
- Requirement that every active account has a value before save.[conversation_history:10]
- Snapshot editing after save, with visible last-edited timestamp.[conversation_history:12]
- Dashboard views for total change since previous snapshot, change by category, latest balances, and total assets over time.[conversation_history:9]

### Explicitly out of scope for v1

- Debts and student loans.[conversation_history:3]
- Custom user-defined categories.[conversation_history:9]
- Internal pots inside accounts.[conversation_history:6]
- Budgeting and recurring bills.[conversation_history:3]
- Open banking or bank integrations.
- Shared household or collaborative multi-user workflows.
- Advanced permission models beyond user-owned data.

## Domain model

The v1 domain should be structured around the following entities:

- User
- Institution
- Account
- Snapshot
- SnapshotEntry

A user owns institutions, accounts, and snapshots. An institution groups accounts so providers such as Halifax can contain multiple accounts while Starling or Trading212 can contain one account now and more later without changing the core model.[conversation_history:6]

A snapshot belongs to a user and represents a complete dated financial picture. A snapshot contains many snapshot entries, each of which stores the balance of one account at that date.[conversation_history:4]

## Categories and reporting logic

Each account is assigned one fixed v1 category:

- Spending money
- Free/liquid capital
- Long-term savings

These categories are intentionally hard-coded for the first release because they directly support the desired reporting views without introducing custom-category management too early. However, category membership should still be stored on the account so the design can evolve into configurable categories later rather than requiring reporting logic to be rewritten.[conversation_history:9][web:118]

## Reporting requirements

The dashboard should prioritise change since the previous snapshot over all other metrics. Good dashboard design works best when the primary metric is obvious and comparison against the previous period is immediate.[web:111][web:119]

Required views for v1:

- Overall change since previous snapshot.[conversation_history:9]
- Current total assets.[conversation_history:9]
- Change since previous snapshot by category.[conversation_history:9]
- Latest balances by account.[conversation_history:9]
- Total assets over time chart.[conversation_history:9]
- Free/liquid capital over time chart.[conversation_history:9]
- Long-term savings over time chart.[conversation_history:9]

## Key user stories

| Priority | User story |
|---|---|
| Must | As a user, I can sign up, log in, and reset my password so my financial data is protected.[conversation_history:11][web:124] |
| Must | As a user, I can create institutions and accounts so I can model my finances accurately.[conversation_history:6] |
| Must | As a user, I can archive accounts so they disappear from new snapshots but remain in my historical records.[conversation_history:5] |
| Must | As a user, I can create a snapshot for today or a chosen date so I can record my financial state over time.[conversation_history:13] |
| Must | As a user, a new snapshot is prefilled from the previous snapshot so I only update changed balances.[conversation_history:5] |
| Must | As a user, I must enter a balance for every active account before saving so my reports remain complete and trustworthy.[conversation_history:10] |
| Must | As a user, I can edit a saved snapshot and see when it was last edited so mistakes can be corrected without hiding that a change was made.[conversation_history:12] |
| Must | As a user, I can see how my total capital changed since the previous snapshot overall and by category.[conversation_history:9] |
| Should | As a user, I can see my total assets over time so I can track longer-term progress.[conversation_history:9] |
| Should | As a user, I can see my latest balances by account for a quick current-state view.[conversation_history:9] |

## Acceptance criteria

- A new snapshot can be created using either a “+ today” action or a chosen date from an adjacent selector.[conversation_history:13]
- A newly created snapshot is prefilled from the most recent previous snapshot for active accounts only.[conversation_history:5]
- A snapshot cannot be saved unless every active account has a value.[conversation_history:10][web:143]
- Archived accounts do not appear in new snapshot forms.[conversation_history:5]
- Historical snapshots still display entries for archived accounts.[conversation_history:5]
- Editing a snapshot updates a visible last-edited timestamp.[conversation_history:12]
- The dashboard shows the delta between the latest snapshot and the immediately previous snapshot.[conversation_history:9]
- Category-level deltas are calculated using the category assigned to each account.[conversation_history:9]

## Non-functional requirements

The product handles personal financial information, so the first release should include secure authentication, user-level data isolation, validation, secure password hashing, HTTPS in production, and a database backup approach. For authenticated MVPs, basic security foundations are launch requirements rather than optional polish.[web:124][web:126][web:131][web:134]

The interface should also optimise for low-friction data entry. Reducing form steps and keeping the core workflow compact is a recognised MVP and UX advantage, especially where regular manual entry is required.[web:40][web:41][web:48]

## Release approach

A sensible release sequence is:

1. Authentication and schema.
2. Institution and account management.
3. Snapshot creation, prefill, and validation.
4. Snapshot history and editing.
5. Dashboard metrics and charts.
6. Hardening, error states, deployment, and backup plan.[web:140][web:148][web:152]
