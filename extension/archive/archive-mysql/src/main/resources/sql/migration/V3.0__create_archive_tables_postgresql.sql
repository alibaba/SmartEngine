-- Migration V3.0: Create archive tables for PostgreSQL
-- Purpose: Archive completed/aborted process instances to keep runtime tables lean
-- This file contains the same DDL as archive-schema-postgre.sql for Flyway/Liquibase migration

-- Source: archive-schema-postgre.sql
-- To use this migration, copy the content from archive-schema-postgre.sql
-- or run archive-schema-postgre.sql directly on your PostgreSQL database.

\i archive-schema-postgre.sql
