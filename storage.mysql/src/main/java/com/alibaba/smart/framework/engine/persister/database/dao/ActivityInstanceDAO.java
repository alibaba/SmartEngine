package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityInstanceDAO {
    List<ActivityInstanceEntity> findAllActivity(Long processInstanceId);

    ActivityInstanceEntity findOne(@Param("id") Long id);

    //@Insert("insert into  se_activity_instance(id, gmt_create, gmt_modified,"
    //    + "        process_instance_id,process_definition_id,process_definition_activity_id) "
    //    + "    values (#{id}, now(), now(), #{processInstanceId}, #{processDefinitionId},"
    //    + "        #{processDefinitionActivityId} ) ")
    //@Options(useGeneratedKeys = true)
    Long insert(  ActivityInstanceEntity activityInstanceEntity );

    Long delete(@Param("id") Long id);

    //@Update("update  activity set   updated_At = #{updatedAt},title =  #{title}, memo = #{memo},start_date=#{startDate},"
    //    + " end_date=#{endDate},type=#{type}, action= #{action},score =#{score},status=#{status},img_id=#{imgId} where id = #{id}")
    //public int updateActivity(Activity activity);
}
