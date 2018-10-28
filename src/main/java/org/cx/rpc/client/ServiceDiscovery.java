package org.cx.rpc.client;

/**
 * @author grass
 * @date 2018/10/28
 */
public interface ServiceDiscovery {

    String discover(String serviceName);
}
