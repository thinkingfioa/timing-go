package org.timing.go.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.timing.go.common.entity.MetaJobHttpEntity;

/**
 * 数据库表 TIMING_GO_META_JOB_HTTP 的Mapper类
 *
 * @author thinking_fioa 2020/4/11
 */
@Mapper
public interface MetaJobHttpMapper {

  /**
   * 更新
   */
  int updateMetaJobHttp(MetaJobHttpEntity entity);

  /**
   * 插入或更新.
   */
  int insertOrUpdateMetaJobHttp(MetaJobHttpEntity entity);
}
