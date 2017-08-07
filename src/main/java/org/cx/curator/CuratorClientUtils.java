package org.cx.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorClientUtils {

  private final static String CONNECTSTRING = "172.16.120.130:2181,172.16.120.131:2181,172.16.120.132:2181";

  private static CuratorFramework curatorFramework;

  public static CuratorFramework getInstance() {
    curatorFramework = CuratorFrameworkFactory.newClient(CONNECTSTRING, 5000, 5000, new ExponentialBackoffRetry(1000, 3));
    curatorFramework.start();
    return curatorFramework;
  }
  
}
