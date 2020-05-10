package org.timing.go.common.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

/**
 * 线程执行器.
 *
 * @author thinking_fioa 2020/5/10
 */
public class ThreadPoolExec {

  private static final int DEFAULT_CORE_SIZE = 1;

  /**
   * 缺省 500ms
   */
  private static final int DEFAULT_KEEP_ALIVE_TIME = 500;

  /**
   * 线程池拒绝策略.
   */
  private static final RejectedExecutionHandler REJECTED_HANDLER = new AbortPolicy();

  private BlockingQueue<Runnable> queue;

  private ThreadPoolExecutor poolExec;

  /**
   * 缺省线程池创建.
   */
  public ThreadPoolExec(int queueCapacity, ThreadFactory threadFactory) {
    queue = new LinkedBlockingQueue<>(queueCapacity);
    poolExec = new ThreadPoolExecutor(DEFAULT_CORE_SIZE, DEFAULT_CORE_SIZE, DEFAULT_KEEP_ALIVE_TIME,
        TimeUnit.MILLISECONDS, queue, threadFactory, REJECTED_HANDLER);
  }

  /**
   * 自定义线程池创建.
   *
   * @param coreSize 核心线程数
   * @param maxSize 最大线程数
   * @param keepAliveTime 核心线程存活时间
   * @param unit 存活时间单位
   * @param queueCapacity 队列最大容量
   * @param threadFactory 线程工厂
   * @param rejectedHandler 拒绝策略
   */
  public ThreadPoolExec(int coreSize, int maxSize, int keepAliveTime, TimeUnit unit,
      int queueCapacity, ThreadFactory threadFactory, RejectedExecutionHandler rejectedHandler) {
    queue = new LinkedBlockingQueue<>(queueCapacity);
    poolExec = new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, unit, queue,
        threadFactory, rejectedHandler);
  }

  public int pendingNum() {
    return queue.size();
  }

  public BlockingQueue<Runnable> getQueue() {
    return queue;
  }

  public ThreadPoolExecutor getPoolExec() {
    return poolExec;
  }
}
