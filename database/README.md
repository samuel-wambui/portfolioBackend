# Portfolio Database

The app uses JPA/Hibernate `ddl-auto=update` in development, so schema changes can be applied by restarting the backend.

These SQL files are optional helpers when you want to create and seed the portfolio data directly.

```bash
psql -d portfolio -f database/schema.sql
psql -d portfolio -f database/seed.sql
```

`seed.sql` resets the portfolio content tables before inserting the sample records.

If you already created the database before profile fields became nullable and are not relying on JPA update, run:

```bash
psql -d portfolio -f database/profile-nullable-migration.sql
```
