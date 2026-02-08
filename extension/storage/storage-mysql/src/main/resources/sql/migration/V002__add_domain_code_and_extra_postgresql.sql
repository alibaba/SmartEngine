-- Add domain_code and extra columns to se_task_instance (PostgreSQL)
ALTER TABLE se_task_instance
    ADD COLUMN domain_code varchar(64) DEFAULT NULL,
    ADD COLUMN extra jsonb DEFAULT NULL;

CREATE INDEX idx_task_domain_code ON se_task_instance(domain_code);
CREATE INDEX idx_task_extra_gin ON se_task_instance USING GIN (extra);
