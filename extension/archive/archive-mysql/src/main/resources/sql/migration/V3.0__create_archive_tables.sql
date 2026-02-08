-- Migration V3.0: Create archive tables for MySQL
-- Purpose: Archive completed/aborted process instances to keep runtime tables lean
-- This file contains the same DDL as archive-schema-mysql.sql for Flyway/Liquibase migration

-- Source: archive-schema-mysql.sql
-- To use this migration, copy the content from archive-schema-mysql.sql
-- or run archive-schema-mysql.sql directly on your MySQL database.

\i archive-schema-mysql.sql
