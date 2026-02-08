-- Add domain_code and extra columns to se_task_instance (Oracle/DM)
ALTER TABLE se_task_instance ADD domain_code VARCHAR2(64) DEFAULT NULL;
ALTER TABLE se_task_instance ADD extra CLOB DEFAULT NULL CHECK (extra IS JSON);

CREATE INDEX idx_task_domain_code ON se_task_instance(domain_code);
