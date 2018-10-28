package org.cx.rpc.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.cx.ConnStringConstant;
import org.cx.rpc.client.loadbalance.LoadBalance;
import org.cx.rpc.client.loadbalance.RandomLoadBalance;

import java.util.ArrayList;
import java.util.List;

/**
 * @author grass
 * @date 2018/10/28
 */
public class ServiceDiscoveryImpl implements ServiceDiscovery {


    List<String> repos=new ArrayList<>();

    private String address;

    private CuratorFramework curatorFramework;

    public ServiceDiscoveryImpl(String address) {
        this.address = address;

        curatorFramework= CuratorFrameworkFactory.builder().
                connectString(address).
                sessionTimeoutMs(4000).
                retryPolicy(new ExponentialBackoffRetry(1000,
                        10)).build();
        curatorFramework.start();
    }
    
    @Override
    public String discover(String serviceName) {
        String path = ConnStringConstant.ZK_REGISTER_PATH + "/" + serviceName;
        try {
            //获取子节点
            repos = curatorFramework.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //动态发现服务节点的变化
        registerWatcher(path);

        LoadBalance loadBalance = new RandomLoadBalance();
        return loadBalance.selectHost(repos);
    }

    /**
     * 监听子节点，是否有修改
     * @param path
     */
    private void registerWatcher(String path) {
        PathChildrenCache childrenCache=new PathChildrenCache(curatorFramework,path,true);

        PathChildrenCacheListener pathChildrenCacheListener=new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                repos=curatorFramework.getChildren().forPath(path);
            }
        };
        childrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            childrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException("注册PatchChild Watcher 异常"+e);
        }

    }
}
