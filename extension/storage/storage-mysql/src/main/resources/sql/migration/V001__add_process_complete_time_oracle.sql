-- Migration: Add complete_time field to se_process_instance table
-- Oracle/DM (DaMeng) compatible syntax
-- Purpose: Track when processes are completed for accurate completed process queries
-- Date: 2026-01-08

ALTER TABLE se_process_instance ADD complete_time TIMESTAMP(6) DEFAULT NULL;

COMMENT ON COLUMN se_process_instance.complete_time IS 'process completion time';

CREATE INDEX idx_complete_time ON se_process_instance (complete_time);
