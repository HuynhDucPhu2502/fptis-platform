DO $$
DECLARE
r RECORD;
BEGIN
FOR r IN (
        SELECT tablename
        FROM pg_tables
        WHERE schemaname = 'public'
        AND (tablename LIKE 'act_re_%'
             OR tablename LIKE 'act_ru_%'
             OR tablename LIKE 'act_hi_%'
             OR tablename LIKE 'act_ge_%'
             OR tablename LIKE 'act_id_%')
    ) LOOP
        RAISE NOTICE 'Dropping table: %', r.tablename;
EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';
END LOOP;

    RAISE NOTICE 'Camunda tables cleanup completed.';
END $$;