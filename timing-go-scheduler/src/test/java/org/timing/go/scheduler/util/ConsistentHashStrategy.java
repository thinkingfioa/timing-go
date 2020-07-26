package org.timing.go.scheduler.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性hash实现
 *
 * @author thinking_fioa 2020/5/30
 */
public class ConsistentHashStrategy {

  /**
   * 复制因子. 虚拟节点 = {@code replicaNum} * 实际节点
   */
  private final int replicaNum;

  /**
   * 虚拟节点映射表
   */
  private final SortedMap<Long, String> circle = new TreeMap<>();

  public ConsistentHashStrategy(int replicaNum, Collection<String> nodes) {
    this.replicaNum = replicaNum;
    for (String node : nodes) {
      add(node);
    }
  }

  /**
   * 对于一个实际机器节点 node, 对应 replicaNum 个虚拟节点
   *
   * 不同的虚拟节点(i不同)有不同的hash值,但都对应同一个实际机器node
   *
   * 虚拟node一般是均衡分布在环上的,数据存储在顺时针方向的虚拟node上
   */
  public void add(String node) {
    for (int i = 0; i < replicaNum; i++) {
      circle.put(hash(node + "_NODE_" + i), node);
    }
  }

  /**
   * 获得一个最近的顺时针节点,根据给定的key计算Hash
   *
   * 然后再取得顺时针方向上最近的一个虚拟节点对应的实际节点，从实际节点中取得数据
   */
  public String get(String key) {
    if (circle.isEmpty()) {
      return null;
    }
    long hash = hash(key);
    if (!circle.containsKey(hash)) {
      SortedMap<Long, String> tailMap = circle.tailMap(hash);
      hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
    }
    return circle.get(hash);
  }

  public long getSize() {
    return circle.size();
  }

  private long hash(String key) {
    MessageDigest md5;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("MD5 algorithm not found");
    }

    md5.reset();
    byte[] keyBytes = null;
    try {
      keyBytes = key.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Unknown string :" + key, e);
    }

    md5.update(keyBytes);
    byte[] digest = md5.digest();

    // hash code, Truncate to 32-bits
    long hashCode = ((long) (digest[3] & 0xFF) << 24)
        | ((long) (digest[2] & 0xFF) << 16)
        | ((long) (digest[1] & 0xFF) << 8)
        | (digest[0] & 0xFF);

    return hashCode & 0xffffffffL;
  }
}
