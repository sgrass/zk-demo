package org.cx.rpc.server;

import org.cx.rpc.api.HelloService;

import java.io.IOException;

/**
 * @author grass
 * @date 2018/10/28
 */
public class ServerDemo {

    public static void main(String[] args) throws IOException {
        HelloService h1 = new HelloServiceImpl();
        HelloService h2 = new HelloServiceImpl2();

        RegisterCenter registerCenter = new RegisterCenterImpl();

        RpcServer rpcServer = new RpcServer(registerCenter, "127.0.0.1:8081");
        rpcServer.bind(h1, h2);
        rpcServer.publisher();
        System.in.read();
    }
}
