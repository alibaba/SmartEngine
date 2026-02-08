-- Oracle/DM (DaMeng) index definitions for SmartEngine
-- Compatible with both Oracle Database and DaMeng Database
-- Includes indexes from both base tables and workflow enhancement tables

-- ===========================================
-- Base table indexes
-- ===========================================

-- se_execution_instance indexes
CREATE INDEX idx_exec_tenant_proc_active ON se_execution_instance (tenant_id, process_instance_id, active);
CREATE INDEX idx_exec_tenant_proc_act ON se_execution_instance (tenant_id, process_instance_id, activity_instance_id);

-- se_task_assignee_instance indexes
CREATE INDEX idx_assignee_tenant_task ON se_task_assignee_instance (tenant_id, task_instance_id);
CREATE INDEX idx_assignee_tenant_id_type ON se_task_assignee_instance (tenant_id, assignee_id, assignee_type);

-- se_activity_instance indexes
CREATE INDEX idx_activity_tenant_proc ON se_activity_instance (tenant_id, process_instance_id);

-- se_deployment_instance indexes
CREATE INDEX idx_deploy_tenant_logic_user ON se_deployment_instance (tenant_id, logic_status, deployment_user_id);

-- se_process_instance indexes
CREATE INDEX idx_proc_tenant_start_user ON se_process_instance (tenant_id, start_user_id);
CREATE INDEX idx_proc_tenant_status ON se_process_instance (tenant_id, status);

-- se_task_instance indexes
CREATE INDEX idx_task_tenant_status ON se_task_instance (tenant_id, status);
CREATE INDEX idx_task_tenant_proc_status ON se_task_instance (tenant_id, process_instance_id, status);
CREATE INDEX idx_task_tenant_def_type ON se_task_instance (tenant_id, process_definition_type);
CREATE INDEX idx_task_tenant_proc ON se_task_instance (tenant_id, process_instance_id);
CREATE INDEX idx_task_tenant_claim_user ON se_task_instance (tenant_id, claim_user_id);
CREATE INDEX idx_task_tenant_tag ON se_task_instance (tenant_id, tag);
CREATE INDEX idx_task_tenant_act_inst ON se_task_instance (tenant_id, activity_instance_id);
CREATE INDEX idx_task_tenant_def_act_id ON se_task_instance (tenant_id, process_definition_activity_id);

-- se_variable_instance indexes
CREATE INDEX idx_var_tenant_proc_exec ON se_variable_instance (tenant_id, process_instance_id, execution_instance_id);

-- ===========================================
-- Workflow enhancement table indexes
-- ===========================================

-- se_supervision_instance indexes
CREATE INDEX idx_supervision_task_status_tenant ON se_supervision_instance (task_instance_id, status, tenant_id);
CREATE INDEX idx_supervision_supervisor_tenant ON se_supervision_instance (supervisor_user_id, tenant_id);
CREATE INDEX idx_supervision_process_instance_id ON se_supervision_instance (process_instance_id);

-- se_notification_instance indexes
CREATE INDEX idx_notification_receiver_read_tenant ON se_notification_instance (receiver_user_id, read_status, tenant_id);
CREATE INDEX idx_notification_sender_tenant ON se_notification_instance (sender_user_id, tenant_id);
CREATE INDEX idx_notification_process_instance_id ON se_notification_instance (process_instance_id);
CREATE INDEX idx_notification_task_instance_id ON se_notification_instance (task_instance_id);

-- se_task_transfer_record indexes
CREATE INDEX idx_task_transfer_task_tenant ON se_task_transfer_record (task_instance_id, tenant_id);
CREATE INDEX idx_task_transfer_from_user_id ON se_task_transfer_record (from_user_id);
CREATE INDEX idx_task_transfer_to_user_id ON se_task_transfer_record (to_user_id);

-- se_assignee_operation_record indexes
CREATE INDEX idx_assignee_op_task_tenant ON se_assignee_operation_record (task_instance_id, tenant_id);
CREATE INDEX idx_assignee_op_operation_type ON se_assignee_operation_record (operation_type);
CREATE INDEX idx_assignee_op_operator_user_id ON se_assignee_operation_record (operator_user_id);
CREATE INDEX idx_assignee_op_target_user_id ON se_assignee_operation_record (target_user_id);

-- se_process_rollback_record indexes
CREATE INDEX idx_rollback_process_tenant ON se_process_rollback_record (process_instance_id, tenant_id);
CREATE INDEX idx_rollback_task_instance_id ON se_process_rollback_record (task_instance_id);
CREATE INDEX idx_rollback_type ON se_process_rollback_record (rollback_type);
CREATE INDEX idx_rollback_operator_user_id ON se_process_rollback_record (operator_user_id);
