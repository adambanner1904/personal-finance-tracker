create trigger set_updated_at_users
before update on users
for each row
execute function set_updated_at();