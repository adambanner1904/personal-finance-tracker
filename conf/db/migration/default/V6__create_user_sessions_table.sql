create table user_sessions (
  session_id uuid primary key default gen_random_uuid(),
  user_id bigint not null references users(id),
  created_at timestamptz not null default now(),
  expires_at timestamptz not null
);
