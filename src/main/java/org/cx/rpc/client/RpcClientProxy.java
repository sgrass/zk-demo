package org.cx.rpc.client;

import java.lang.reflect.Proxy;

/**
 * @author grass
 * @date 2018/10/28
 */
public class RpcClientProxy {

    private ServiceDiscovery serviceDiscovery;

    public RpcClientProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    /**
     * 创建客户端的远程代理。通过远程代理进行访问
     * @param interfaceCls
     * @param <T>
     * @return
     */
    public <T> T clientProxy(final Class<T> interfaceCls,String version){
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(),
                new Class[]{interfaceCls},new RemoteInvocationHandler(serviceDiscovery,version));
    }
}
