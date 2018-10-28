package org.cx.rpc.server;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.cx.ConnStringConstant;

/**
 * @author grass
 * @date 2018/10/28
 */
public class RegisterCenterImpl implements RegisterCenter {
    private CuratorFramework curatorFramework;

    {
        curatorFramework= CuratorFrameworkFactory.builder().
                connectString(ConnStringConstant.CONNECT_STRING)
                .sessionTimeoutMs(4000).retryPolicy(
                        new ExponentialBackoffRetry(1000,10))
                .build();

        curatorFramework.start();
    }

    @Override
    public void register(String serviceName, String address) {

        String servicePath = ConnStringConstant.ZK_REGISTER_PATH + "/" + serviceName;

        try {
            //判断 /registrys/product-service是否存在，不存在则创建
            if(curatorFramework.checkExists().forPath(servicePath)==null){
                curatorFramework.create().creatingParentsIfNeeded().
                        withMode(CreateMode.PERSISTENT).forPath(servicePath,"0".getBytes());
            }

            String addressPath = servicePath + "/" + address;

            String rsNode=curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(addressPath,"0".getBytes());

            System.out.println("服务注册成功："+rsNode);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
