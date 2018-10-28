package org.cx.rpc.client.loadbalance;

import java.util.List;

/**
 * @author grass
 * @date 2018/10/28
 */
public interface LoadBalance {
    String selectHost(List<String> repos);
}
