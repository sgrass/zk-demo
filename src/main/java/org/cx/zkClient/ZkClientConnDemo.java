package org.cx.zkClient;

import org.I0Itec.zkclient.ZkClient;

public class ZkClientConnDemo {
  
  private final static String CONNECTSTRING = "172.16.120.130:2181,172.16.120.131:2181,172.16.120.132:2181";
  
  public static void main(String[] args) {
    ZkClient zc = new ZkClient(CONNECTSTRING,5000);
    System.out.println(zc+"-->success");
  }
}
