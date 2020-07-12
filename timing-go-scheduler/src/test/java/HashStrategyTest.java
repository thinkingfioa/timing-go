package org.timing.go.scheduler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.timing.go.common.util.GsonUtils;
import org.timing.go.common.util.StringUtils;
import org.timing.go.scheduler.util.AverageStrategy;
import org.timing.go.scheduler.util.ConsistentHashStrategy;

/**
 * @author thinking_fioa 2020/5/30
 */
public class HashStrategyTest {

  private static List<String> exectorAddressList = new ArrayList<>();

  private static int taskNum = 15;
  private static int shardingNum = 100;

  private static int firstExectorCount = 3;
  private static int secondExectorCount = 8;

  private static int replicaNum = 100;

  public static void main(String[] args) {
    System.out.println("**************************");

    exectorAddressList.clear();
    for (int i = 1; i <= firstExectorCount; i++) {
      exectorAddressList.add("Executor_" + i);
    }
    Map<String, String> avgMap = calAvg(taskNum, shardingNum, exectorAddressList);
    Map<String, String> consistentMap = calConsistent(taskNum, shardingNum, exectorAddressList);

    System.out.println(GsonUtils.toPrettyJson(avgMap));
    System.out.println(GsonUtils.toPrettyJson(consistentMap));
    outputExectorNum(avgMap);
    outputExectorNum(consistentMap);

    System.out.println("**************************");

    exectorAddressList.clear();
    for (int i = 1; i <= secondExectorCount; i++) {
      exectorAddressList.add("Executor_" + i);
    }
    Map<String, String> avgMap2 = calAvg(taskNum, shardingNum, exectorAddressList);
    Map<String, String> consistentMap2 = calConsistent(taskNum, shardingNum, exectorAddressList);

    System.out.println(GsonUtils.toPrettyJson(avgMap2));
    System.out.println(GsonUtils.toPrettyJson(consistentMap2));
    outputExectorNum(avgMap2);
    outputExectorNum(consistentMap2);
    System.out.println("**************************");
    System.out.println(calDiff(avgMap, avgMap2));
    System.out.println(calDiff(consistentMap, consistentMap2));
  }

  private static void outputExectorNum(Map<String, String> map) {
    Map<String, Integer> result = new TreeMap<>();
    Set<Entry<String, String>> entrySet = map.entrySet();
    for (Entry<String, String> entry : entrySet) {
      String address = entry.getValue();
      result.computeIfAbsent(address, v -> 0);
      result.put(address, result.get(address) + 1);
    }
    System.out.println(GsonUtils.toPrettyJson(result));
  }

  /**
   * key taskNo_shadingNum, values ExecutorID.
   */
  private static int calDiff(Map<String, String> map1, Map<String, String> map2) {
    int diffCount = 0;
    Set<Entry<String, String>> entrySet = map1.entrySet();
    for (Entry<String, String> entry : entrySet) {
      String key = entry.getKey();
      if (!StringUtils.equalAfterTrim(entry.getValue(), map2.get(key))) {
        diffCount++;
      }
    }
    return diffCount;
  }

  private static Map<String, String> calAvg(int taskNum, int shardingNum,
      List<String> exectorAddressList) {
    // 平均
    AverageStrategy avgStrategy = new AverageStrategy(exectorAddressList);
    Map<String, String> avgResultMap = new LinkedHashMap<>();
    for (int singleTask = 1; singleTask <= taskNum; singleTask++) {
      for (int singleSharding = 1; singleSharding <= shardingNum; singleSharding++) {
        String key = singleTask + "_" + singleSharding;
        avgResultMap.put(key, avgStrategy.getNode(singleSharding));
      }
    }
    return avgResultMap;
  }

  private static Map<String, String> calConsistent(int taskNum, int shardingNum,
      List<String> exectorAddressList) {
    // 一致性Hash
    ConsistentHashStrategy consistentStrategy
        = new ConsistentHashStrategy(replicaNum, exectorAddressList);
    Map<String, String> hashResultMap = new LinkedHashMap<>();
    for (int singleTask = 1; singleTask <= taskNum; singleTask++) {
      for (int singleSharding = 1; singleSharding <= shardingNum; singleSharding++) {
        String key = singleTask + "_" + singleSharding;
        hashResultMap.put(key, consistentStrategy.get(key));
      }
    }
    return hashResultMap;
  }

}
