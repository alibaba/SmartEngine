

alter table `se_execution_instance`
    add key `idx_tenant_id_process_instance_id_and_status_tenant_id` (tenant_id,process_instance_id,active),
    add key `idx_tenant_id_process_instance_id_and_activity_instance_id` (tenant_id,process_instance_id,activity_instance_id);

alter table `se_task_assignee_instance`
    add key `idx_tenant_id_task_instance_id` (tenant_id,task_instance_id),
    add key `idx_tenant_id_assignee_id_and_type` (tenant_id,assignee_id,assignee_type);

alter table `se_activity_instance`
    add key `idx_tenant_id_process_instance_id` (tenant_id,process_instance_id);

alter table `se_deployment_instance`
    add key `idx_tenant_id_user_id_and_logic_status` (tenant_id,logic_status,deployment_user_id);

alter table `se_process_instance`
    add key `idx_tenant_id_start_user_id` (tenant_id,start_user_id),
    add key `idx_tenant_id_status` (tenant_id,status);

alter table `se_task_instance`
    add key `idx_tenant_id_status` (tenant_id,status),
    add key `idx_tenant_id_process_instance_id_and_status` (tenant_id,process_instance_id,status),
    add key `idx_tenant_id_process_definition_type` (tenant_id,process_definition_type),
    add key `idx_tenant_id_process_instance_id` (tenant_id,process_instance_id),
    add key `idx_tenant_id_claim_user_id` (tenant_id,claim_user_id),
    add key `idx_tenant_id_tag` (tenant_id,tag),
    add key `idx_tenant_id_activity_instance_id` (tenant_id,activity_instance_id),
    add key `idx_tenant_id_process_definition_activity_id` (tenant_id,process_definition_activity_id);

alter table `se_variable_instance`
    add key `idx_tenant_id_process_instance_id_and_execution_instance_id` (tenant_id,process_instance_id,execution_instance_id);
