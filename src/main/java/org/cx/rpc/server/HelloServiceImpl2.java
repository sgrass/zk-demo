package org.cx.rpc.server;

import org.cx.rpc.api.HelloService;

/**
 * @author grass
 * @date 2018/10/28
 */
@RpcAnnotation(HelloService.class)
public class HelloServiceImpl2 implements HelloService {

    protected HelloServiceImpl2() {
    }

    @Override
    public String sayHello(String msg) {
        return "hello2222222, " + msg;
    }
}
