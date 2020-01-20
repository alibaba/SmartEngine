

alter table `se_execution_instance`
add key `idx_process_instance_id_and_status` (process_instance_id,active),
add key `idx_process_instance_id_and_activity_instance_id` (process_instance_id,activity_instance_id);

alter table `se_task_assignee_instance`
add key `idx_task_instance_id` (task_instance_id),
add key `idx_assignee_id_and_type` (assignee_id,assignee_type);

alter table `se_activity_instance`
add key `idx_process_instance_id` (process_instance_id);

alter table `se_deployment_instance`
add key `idx_user_id_and_logic_status` (logic_status,deployment_user_id);

alter table `se_process_instance`
add key `idx_start_user_id` (start_user_id),
add key `idx_status` (status);

alter table `se_task_instance`
add key `idx_status` (status),
add key `idx_process_instance_id_and_status` (process_instance_id,status),
add key `idx_process_definition_type` (process_definition_type),
add key `idx_process_instance_id` (process_instance_id),
add key `idx_claim_user_id` (claim_user_id),
add key `idx_tag` (tag),
add key `idx_activity_instance_id` (activity_instance_id),
add key `idx_process_definition_activity_id` (process_definition_activity_id);

alter table `se_variable_instance`
add key `idx_process_instance_id_and_execution_instance_id` (process_instance_id,execution_instance_id);
