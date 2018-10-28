package org.cx.rpc.client.loadbalance;

import java.util.List;

/**
 * @author grass
 * @date 2018/10/28
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public String selectHost(List<String> repos) {
        if (repos == null || repos.size() == 0) {
            return null;
        }
        if (repos.size() == 1) {
            return repos.get(0);
        }
        return doSelect(repos);
    }

    protected abstract String doSelect(List<String> repos);

}
