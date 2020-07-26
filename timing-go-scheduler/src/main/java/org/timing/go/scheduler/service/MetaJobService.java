package org.timing.go.scheduler.service;

import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.timing.go.common.entity.MetaJobEntity;
import org.timing.go.common.entity.MetaJobHttpEntity;
import org.timing.go.common.mapper.MetaJobHttpMapper;
import org.timing.go.common.mapper.MetaJobMapper;
import org.timing.go.common.zkbean.MetaJobHttpZkBean;

/**
 * 元Job服务.
 *
 * @author thinking_fioa 2020/5/9
 */
@Service
public class MetaJobService {

  private static final Logger LOGGER = LogManager.getLogger(MetaJobService.class);

  @Resource
  private MetaJobMapper metaJobMapper;

  @Resource
  private MetaJobHttpMapper metaJobHttpMapper;

  /**
   * 插入或更新MetaJobHttp的元Job
   */
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
  public int insertOrUpdateMetaJobHttp(MetaJobHttpZkBean metaJobHttpZkBean) {
    String metaJobKey = metaJobHttpZkBean.getMetaJobKey();
    String groupName = metaJobHttpZkBean.getGroupName();
    MetaJobEntity metaJobOld = metaJobMapper.selectMetaJob(metaJobKey, groupName);
    if (null != metaJobOld) {
      LOGGER.info("update MetaJobHttp. metaJobKey {}, groupName {}", metaJobKey, groupName);
      metaJobMapper.updateMetaJob(new MetaJobEntity(metaJobHttpZkBean));
      return metaJobHttpMapper.updateMetaJobHttp(new MetaJobHttpEntity(metaJobHttpZkBean));
    }
    LOGGER.info("insert MetaJobHttp. metaJobKey {}, groupName {}", metaJobKey, groupName);

    metaJobMapper.insertOrUpdateMetaJob(new MetaJobEntity(metaJobHttpZkBean));
    return metaJobHttpMapper.insertOrUpdateMetaJobHttp(new MetaJobHttpEntity(metaJobHttpZkBean));
  }
}
