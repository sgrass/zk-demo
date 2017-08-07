package org.cx.zkClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class ZkCLientOperator {

  private final static String CONNECTSTRING = "172.16.120.130:2181,172.16.120.131:2181,172.16.120.132:2181";

  private static ZkClient getInstance() {
    return new ZkClient(CONNECTSTRING, 5000);
  }

  public static void main(String[] args) throws InterruptedException {
    ZkClient zkClient = getInstance();
    // zkclient 提供递归创建父节点的功能
    zkClient.createPersistent("/zkclient/zkclient1/zkclient1-1/zkclient1-1-1", true);
    System.out.println("success");

    // 删除节点
    zkClient.deleteRecursive("/zkclient");

    // 获取子节点
    List<String> list = zkClient.getChildren("/node");
    System.out.println(list);

    // watcher

    zkClient.subscribeDataChanges("/node", new IZkDataListener() {
      @Override
      public void handleDataChange(String s, Object o) throws Exception {
        System.out.println("节点名称：" + s + "->节点修改后的值" + o);
      }

      @Override
      public void handleDataDeleted(String s) throws Exception {

      }
    });

    zkClient.writeData("/node", "node");
    TimeUnit.SECONDS.sleep(2);

    zkClient.subscribeChildChanges("/node", new IZkChildListener() {
      @Override
      public void handleChildChange(String s, List<String> list) throws Exception {
        System.out.println("节点名称：" + s + "->节点修改后的值" + list);
      }
    });
    
    zkClient.writeData("/node/node1", "222");
    TimeUnit.SECONDS.sleep(2);
  }
}
