package org.cx.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.cx.ConnStringConstant;

public class CuratorCreateDemo {


  public static void main(String[] args) {

    // 创建会话的两种方式 normal
    CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(ConnStringConstant.CONNECT_STRING, 5000, 5000, new ExponentialBackoffRetry(1000, 3));
    curatorFramework.start(); // start方法启动连接

    /**
     * ExponentialBackoffRetry()  衰减重试 
     * RetryNTimes 指定最大重试次数
     * RetryOneTime 仅重试一次
     * RetryUnitilElapsed 一直重试知道规定的时间
     */
    // fluent风格
    CuratorFramework curatorFramework1 = CuratorFrameworkFactory.builder().connectString(ConnStringConstant.CONNECT_STRING)
        .sessionTimeoutMs(5000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
        .namespace("curator").build();

    curatorFramework1.start();
    System.out.println("success");
  }
}
