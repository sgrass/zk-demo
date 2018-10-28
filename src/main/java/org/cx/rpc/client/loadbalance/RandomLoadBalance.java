package org.cx.rpc.client.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * @author grass
 * @date 2018/10/28
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String doSelect(List<String> repos) {
        Random random = new Random();
        return repos.get(random.nextInt(repos.size()));
    }
}
