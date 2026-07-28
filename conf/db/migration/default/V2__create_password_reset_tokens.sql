create table password_reset_tokens (
    id bigint generated always as identity primary key,
    user_id bigint not null references users(id) on delete cascade,
    token_hash text not null,
    expires_at timestamptz not null,
    used_at timestamptz,
    created_at timestamptz not null default now()
);

create index idx_password_reset_tokens_user_id on password_reset_tokens(user_id);