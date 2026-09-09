flyway \
  -url="jdbc:postgresql://localhost:5432/personal_finance_tracker" \
  -user="pft_app" \
  -password="finance" \
  -locations="filesystem:conf/db/migration/default" \
  -schemas="public" \
  -defaultSchema="public" \
  migrate
