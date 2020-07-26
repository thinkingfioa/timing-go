package org.timing.go.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.timing.go.common.entity.MetaJobEntity;

/**
 * 数据库表 TIMING_GO_META_JOB 的Mapper类
 *
 * @author thinking_fioa 2020/4/11
 */
@Mapper
public interface MetaJobMapper {

  MetaJobEntity selectMetaJob(@Param("metaJobKey") String metaJobKey,
      @Param("groupName") String groupName);

  int updateMetaJob(MetaJobEntity entity);

  int insertOrUpdateMetaJob(MetaJobEntity entity);
}
