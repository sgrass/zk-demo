package org.cx.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.cx.ConnStringConstant;

public class CuratorClientUtils {


  private static CuratorFramework curatorFramework;

  public static CuratorFramework getInstance() {
    curatorFramework = CuratorFrameworkFactory.newClient(ConnStringConstant.CONNECT_STRING, 5000, 5000, new ExponentialBackoffRetry(1000, 3));
    curatorFramework.start();
    return curatorFramework;
  }
  
}
