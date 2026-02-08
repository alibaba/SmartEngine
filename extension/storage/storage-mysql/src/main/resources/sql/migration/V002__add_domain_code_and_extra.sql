-- Add domain_code and extra columns to se_task_instance (MySQL)
ALTER TABLE se_task_instance
    ADD COLUMN domain_code varchar(64) DEFAULT NULL COMMENT 'domain code',
    ADD COLUMN extra json DEFAULT NULL COMMENT 'extra JSON data';

CREATE INDEX idx_task_domain_code ON se_task_instance(domain_code);
