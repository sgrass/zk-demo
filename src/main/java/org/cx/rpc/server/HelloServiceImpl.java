package org.cx.rpc.server;

import org.cx.rpc.api.HelloService;

/**
 * @author grass
 * @date 2018/10/28
 */
@RpcAnnotation(HelloService.class)
public class HelloServiceImpl implements HelloService {

    protected HelloServiceImpl() {
    }

    @Override
    public String sayHello(String msg) {
        return "hello, " + msg;
    }
}
