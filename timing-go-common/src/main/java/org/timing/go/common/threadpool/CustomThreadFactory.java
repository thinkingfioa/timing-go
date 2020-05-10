package org.timing.go.common.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.timing.go.common.CommonConstant;

/**
 * 线程工厂，创建线程.
 *
 * @author wllu
 */
public class CustomThreadFactory implements ThreadFactory {

  /**
   * 线程名前缀.
   */
  private final String threadNamePrefix;
  private final boolean daemon;

  private AtomicInteger createdThreadCount = new AtomicInteger(1);

  public CustomThreadFactory(final String namePrefix, final boolean daemon) {
    threadNamePrefix = namePrefix;
    this.daemon = daemon;
  }

  @Override
  public Thread newThread(Runnable r) {
    final String threadName =
        threadNamePrefix + CommonConstant.UNDERLINE + createdThreadCount.getAndIncrement();
    Thread t = new Thread(r, threadName);
    t.setDaemon(daemon);
    return t;
  }
}
