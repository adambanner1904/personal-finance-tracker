create table users (
    id bigint generated always as identity primary key,
    email citext not null unique,
    password_hash text not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create index idx_users_email on users(email);