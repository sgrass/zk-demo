package org.cx;

import java.sql.Time;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class ZkTemp {

  private static Stat stat = new Stat();

  
  public static void main(String[] args) throws Exception {
    
    CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(ConnStringConstant.CONNECT_STRING)
        .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).namespace("temp").build();
    cf.start();
    
    InterProcessMutex lock = new InterProcessMutex(cf, "/lock");
    if (lock.acquire(1, TimeUnit.SECONDS)) {
      try {
        
      } finally {
        lock.release();
      }
    }
  }

}
