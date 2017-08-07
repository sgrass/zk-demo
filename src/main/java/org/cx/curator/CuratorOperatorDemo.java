package org.cx.curator;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class CuratorOperatorDemo {

  public static void main(String[] args) throws Exception {
    CuratorFramework curatorFramework = CuratorClientUtils.getInstance();
    System.out.println("连接成功.........");

    // 创建节点 fluent链式风格
//    String result = curatorFramework.create().creatingParentsIfNeeded()
//        .withMode(CreateMode.PERSISTENT).forPath("/curator/curator1/curator11", "123".getBytes());
//
//    System.out.println(result);

    //删除节点
//    curatorFramework.delete().deletingChildrenIfNeeded().forPath("/node");
    
    //查询节点
    Stat stat=new Stat();
    byte[] bytes=curatorFramework.getData().storingStatIn(stat).forPath("/curator");
    System.out.println(new String(bytes)+"-->stat:"+stat);
    
    //更新
    Stat st=curatorFramework.setData().forPath("/curator","123".getBytes());
    System.out.println(st);
    
    
    /**
     * 异步操作
     */
    ExecutorService service= Executors.newFixedThreadPool(1);
    CountDownLatch countDownLatch=new CountDownLatch(1);
    curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
      .inBackground(new BackgroundCallback() {
          @Override
          public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
            System.out.println(Thread.currentThread().getName()+"->resultCode:"+curatorEvent.getResultCode()+"->" +curatorEvent.getType());
            countDownLatch.countDown();
          }
      },service).forPath("/mic","123".getBytes());
    countDownLatch.await();
    service.shutdown();
    
    
    /**
     * 事务操作（curator独有的）
     */
    Collection<CuratorTransactionResult> resultCollections=curatorFramework.inTransaction()
        .create().withMode(CreateMode.EPHEMERAL).forPath("/trans","111".getBytes()).and()
        .setData().forPath("/curator","111".getBytes()).and().commit();
    
    for (CuratorTransactionResult result:resultCollections){
        System.out.println(result.getForPath()+"->"+result.getType());
    }
    
  }
}
