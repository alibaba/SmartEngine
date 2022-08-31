package com.alibaba.smart.framework.engine.constant;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  20:55.
 *
 * NOT the interface TYPE.
 */
public class RequestMapSpecialKeyConstant {

    public static final String $_SMART_ENGINE_$_PREFIX = "_$_smart_engine_$_";

    public static final String PROCESS_DEFINITION_TYPE = $_SMART_ENGINE_$_PREFIX + "process_definition_type";

    public static final String PROCESS_INSTANCE_START_USER_ID = $_SMART_ENGINE_$_PREFIX + "start_user_id";

    public static final  String PROCESS_BIZ_UNIQUE_ID = $_SMART_ENGINE_$_PREFIX + "biz_unique_id";

    public static final  String PROCESS_TITLE = $_SMART_ENGINE_$_PREFIX + "PROCESS_TITLE";

    public static final  String PROCESS_INSTANCE_COMMENT = $_SMART_ENGINE_$_PREFIX + "PROCESS_INSTANCE_COMMENT";

    public static final  String PROCESS_INSTANCE_ABORT_REASON = $_SMART_ENGINE_$_PREFIX + "PROCESS_INSTANCE_ABORT_REASON";


    public static final  String TASK_START_TIME = $_SMART_ENGINE_$_PREFIX + "task_start_time";

    public static final  String TASK_COMPLETE_TIME = $_SMART_ENGINE_$_PREFIX + "task_complete_time";


    public static final  String TASK_INSTANCE_TAG = $_SMART_ENGINE_$_PREFIX + "task_instance_tag";

    //TODO 去掉instance 非兼容升级

    public static final  String TASK_INSTANCE_EXTENSION = $_SMART_ENGINE_$_PREFIX + "task_instance_extension";

    public static final  String TASK_INSTANCE_PRIORITY = $_SMART_ENGINE_$_PREFIX + "task_instance_priority";

    public static final  String TASK_TITLE = $_SMART_ENGINE_$_PREFIX + "task_title";

    public static final  String TASK_EXTENSION = $_SMART_ENGINE_$_PREFIX + "task_extension";


    //TODO 修改为小写 非兼容升级
    public static final  String TASK_INSTANCE_CLAIM_USER_ID = $_SMART_ENGINE_$_PREFIX + "TASK_INSTANCE_CLAIM_USER_ID";

    public static final  String TASK_INSTANCE_COMMENT = $_SMART_ENGINE_$_PREFIX + "TASK_INSTANCE_COMMENT";



    public static final  String CLAIM_USER_ID = $_SMART_ENGINE_$_PREFIX + "claimUserId";

    public static final  String CLAIM_USER_TIME = $_SMART_ENGINE_$_PREFIX + "claimUserTime";

    public static final  String LATCH_WAIT_TIME_IN_MILLISECOND = $_SMART_ENGINE_$_PREFIX + "latchWaitTime";

}
