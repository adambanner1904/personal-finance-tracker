create table institutions (
    id bigint generated always as identity primary key,
    user_id bigint not null references users(id),
    name text not null,
    archived_at timestamptz,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    unique(user_id, name)
);

create table accounts (
    id bigint generated always as identity primary key,
    institution_id bigint not null references institutions(id),
    name text not null,
    archived_at timestamptz,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create index idx_accounts_institution_id_archived_at on accounts(institution_id, archived_at);

create table snapshots (
    id bigint generated always as identity primary key, 
    user_id bigint not null references users(id),
    snapshot_date date not null default current_date,
    notes text,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    unique(user_id, snapshot_date) -- only one snapshot per a user for a day
);

create table snapshot_entries (
    id bigint generated always as identity primary key,
    snapshot_id bigint not null references snapshots(id) on delete cascade, -- if you do delete a snapshot all entries should be deleted too
    account_id bigint not null references accounts(id),
    balance numeric(14, 2) not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    unique(snapshot_id, account_id) -- only one entry for one account per a snapshot
);

create or replace function set_updated_at()
returns trigger as $$
begin
    new.updated_at = now();
    return new;
end;
$$ language plpgsql;

create trigger set_updated_at_institutions 
before update on institutions
for each row 
execute function set_updated_at();

create trigger set_updated_at_accounts
before update on accounts
for each row
execute function set_updated_at();

create trigger set_updated_at_snapshots
before update on snapshots
for each row
execute function set_updated_at();

create trigger set_updated_at_snapshot_entries
before update on snapshot_entries
for each row
execute function set_updated_at();