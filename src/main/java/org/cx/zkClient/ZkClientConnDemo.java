package org.cx.zkClient;

import org.I0Itec.zkclient.ZkClient;
import org.cx.ConnStringConstant;

public class ZkClientConnDemo {
  
  public static void main(String[] args) {
    ZkClient zc = new ZkClient(ConnStringConstant.CONNECT_STRING,5000);
    System.out.println(zc+"-->success");
  }
}
