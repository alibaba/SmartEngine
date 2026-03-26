-- Migration V3.0: Create archive tables for Oracle/DM
-- Purpose: Archive completed/aborted process instances to keep runtime tables lean
-- This file contains the same DDL as archive-schema-oracle.sql for Flyway/Liquibase migration

-- Source: archive-schema-oracle.sql
-- To use this migration, copy the content from archive-schema-oracle.sql
-- or run archive-schema-oracle.sql directly on your Oracle/DM database.

@archive-schema-oracle.sql
