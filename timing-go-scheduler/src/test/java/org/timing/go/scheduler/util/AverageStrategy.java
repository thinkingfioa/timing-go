package org.timing.go.scheduler.util;

import java.util.Collections;
import java.util.List;

/**
 * 平均分配策略
 *
 * @author thinking_fioa 2020/5/30
 */
public class AverageStrategy {

  private List<String> nodeList;

  public AverageStrategy(List<String> nodeList) {
    this.nodeList = Collections.unmodifiableList(nodeList);
  }

  public String getNode(int sharding) {
    return nodeList.get((sharding - 1) % (nodeList.size()));
  }
}
