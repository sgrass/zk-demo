package org.cx.rpc.client;

import org.cx.ConnStringConstant;
import org.cx.rpc.api.HelloService;

/**
 * @author grass
 * @date 2018/10/28
 */
public class ClientDemo {

    public static void main(String[] args) throws InterruptedException {
        ServiceDiscovery serviceDiscovery=new
                ServiceDiscoveryImpl(ConnStringConstant.CONNECT_STRING);

        RpcClientProxy rpcClientProxy=new RpcClientProxy(serviceDiscovery);

        for(int i=0;i<10;i++) {
            HelloService hello = rpcClientProxy.clientProxy(HelloService.class, null);
            System.out.println(hello.sayHello("12333"));
            Thread.sleep(1000);
        }
    }
}
