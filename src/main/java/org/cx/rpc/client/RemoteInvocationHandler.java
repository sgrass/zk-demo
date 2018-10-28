package org.cx.rpc.client;

import org.cx.rpc.server.RpcRequest;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author grass
 * @date 2018/10/28
 */
public class RemoteInvocationHandler implements InvocationHandler {
    private ServiceDiscovery serviceDiscovery;

    private String version;

    public RemoteInvocationHandler(ServiceDiscovery serviceDiscovery, String version) {
        this.serviceDiscovery = serviceDiscovery;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //组装请求
        RpcRequest request=new RpcRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setVersion(version);

        String serviceAddress=serviceDiscovery.discover(request.getClassName()); //根据接口名称得到对应的服务地址
        //通过tcp传输协议进行传输
        TCPTransport tcpTransport=new TCPTransport(serviceAddress);
        //发送请求
        return tcpTransport.send(request);
    }
}
