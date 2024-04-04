-- Role: courses_admin
DO
$do$
    BEGIN
        IF NOT EXISTS (
                SELECT 1
                FROM pg_catalog.pg_roles
                WHERE rolname = 'courses_admin'
            ) THEN
            CREATE ROLE courses_admin WITH
                LOGIN
                NOSUPERUSER
                CREATEDB
                NOCREATEROLE
                INHERIT
                NOREPLICATION
                CONNECTION LIMIT -1
                PASSWORD '1234';
        END IF;
    END
$do$;

SELECT rolname, rolsuper, rolinherit, rolcreatedb, rolcreaterole, rolcanlogin
FROM pg_roles;
