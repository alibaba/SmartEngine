# 3.6.0-SNAPSHOT
1. [Feature Enhancement] Add multi-tenancy feature, thanks to @yanricheng1 for submitting the PR.
2. [Module adjustment] Remove MongoDB Storage


# 3.5.0
1. [Feature Enhancement] Full support for Inclusive Gateway, including default seqFlow, nested inclusive gateways and unbalanced gateways
2. [Refinement] Refactored processing logic for inclusive/parallel/exclusive gateways to reduce code duplication
3. [Minor Breaking Change] Added `block_id` field to `se_execution_instance` table to support inclusive gateways
4. [Bug Fix] Fixed abnormal behavior in nested parallel gateway scenarios
5. [Minor Breaking Change] Modified method signature of `VariablePersister#deserialize` for more flexible deserialization

# 3.0.0
1. Enhanced testing for nested parallel gateways and sub-processes under multi-threading conditions
2. CallActivity: Isolated request/response between parent/child processes (shared data can be manually retrieved from parentContext)
3. Added RetryServiceTaskTest

# 2.6.4
1. [Minor Breaking Change] Modified IdGenerator implementation strategy - now requires manual instance assignment after implementing ID strategy (temporary regression to prepare for auto-increment PK and granular ID generation)
2. [Minor Breaking Change] Changed return type of TaskCommandService#complete from void to ProcessInstance

# 2.6.3
1. Added support for DefaultSequenceFlow in exclusive gateways; optimized concurrency logic in parallel gateways for better readability

# 2.6.2
1. [Minor Breaking Change] Modified second parameter of TaskAssigneeDispatcher#getTaskAssigneeCandidateInstance from Map to Context for accessing process definition parameters

# 2.6.1
1. Added hook mechanism in execute() method to allow canceling process progression in specific business scenarios

# 2.6.0
1. [Critical Bug Fix] Fixed memory leak in multi-threaded service orchestration using parallel gateways (strongly recommended upgrade for affected users)

# 2.5.7.2
1. Added query support for process instance start/end times; enabled process end event triggering

# 2.5.7.1
1. Optimized ACTIVITY_START event timing to trigger after node creation

# 2.5.7.0
1. [Breaking Change] Added PropertyCompositeValue for Properties attributes only

# 2.5.6.x
1. Added PID parameter for signal queries (preparation for sharding)

# 2.5.1
1. [Breaking Change] LockStrategy: Unified process flow and parallel gateway join handling with Context parameter for granular lock control; fixed sub-process compatibility issues

# 2.5.0
1. Added "anyof" mode support for parallel gateways (see `ServiceOrchestrationParallelGatewayTest`)
2. Added Camunda Modeler compatibility (v4.0.0 tag) with support for smart:class, smart:properties, and smart:eventListener attributes

# 2.2.2
0. Improved parent-child processes and parallel gateway orchestration logic
1. [Breaking Change] Replaced MVEL expression engine COC convention with init parameter (better performance and ClassLoader handling)
2. Added default ParallelServiceOrchestration implementation for business extensions
3. Supported multiple OutComingTransitions to simplify exclusive gateway modeling

# 2.2.3
0. [Database Bug Fix] Added parent_execution_instance_id field to se_process_instance to fix parent process continuation after sub-process completion

# 2.2.3.1
0. Fixed transfer functionality with added test coverage (reinforced the importance of testing)