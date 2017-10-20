package org.cx.curator;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.CreateMode;

public class CuratorEventDemo {

  /**
   * 三种watcher来做节点的监听 
   * pathcache 监视一个路径下子节点的创建、删除、节点数据更新 
   * NodeCache 监视一个节点的创建、更新、删除 
   * TreeCache pathcaceh+nodecache 的合体（监视路径下的创建、更新、删除事件），
   * 缓存路径下的所有子节点的数据
   * 
   */
  public static void main(String[] args) throws Exception {
    CuratorFramework curatorFramework=CuratorClientUtils.getInstance();

    /**
     * 节点变化NodeCache
     */
    NodeCache cache=new NodeCache(curatorFramework,"/curator",false);
    cache.start(true);

    cache.getListenable().addListener(()-> System.out.println("节点数据发生变化,变化后的结果" + "：" + new String(cache.getCurrentData().getData())));

    curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/curator","111".getBytes());
    curatorFramework.setData().forPath("/curator","111".getBytes());
    TimeUnit.SECONDS.sleep(1);
    

    /**
     * PatchChildrenCache
     * StartMode三种模式
     * Normal 初始化时候为空
     * BUILD_INITIAL_CACHE 方法返回之前调用rebuild
     * POST_INITIALIZED_EVENT 初始化后发送一个cacheevent的事件
     */

    PathChildrenCache pc=new PathChildrenCache(curatorFramework,"/event",true);
    pc.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
    // Normal / BUILD_INITIAL_CACHE /POST_INITIALIZED_EVENT

    pc.getListenable().addListener((curatorFramework1,pathChildrenCacheEvent)->{
        switch (pathChildrenCacheEvent.getType()){
            case CHILD_ADDED:
                System.out.println("增加子节点");
                break;
            case CHILD_REMOVED:
                System.out.println("删除子节点");
                break;
            case CHILD_UPDATED:
                System.out.println("更新子节点");
                break;
            default:break;
        }
    });

    curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/event","event".getBytes());
    TimeUnit.SECONDS.sleep(1);
    System.out.println("1");
    
    curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("/event/event1","1".getBytes());
    TimeUnit.SECONDS.sleep(1);
    System.out.println("2");

    curatorFramework.setData().forPath("/event/event1","222".getBytes());
    TimeUnit.SECONDS.sleep(1);
    System.out.println("3");

    curatorFramework.delete().deletingChildrenIfNeeded().forPath("/event");
    System.out.println("4");


    
    System.out.println("\n\n");
    /**
     * treeCache
     */
    TreeCache tc = new TreeCache(curatorFramework, "/event");
    tc.start();
    tc.getListenable().addListener(new TreeCacheListener() {
      
      @Override
      public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
        System.out.println(event.getType()+"-->"+event.getData()+"----"+client.getState());
      }
    });
    
    curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/event/event-1","11".getBytes());
    curatorFramework.setData().forPath("/event/event-1", "22".getBytes());
    curatorFramework.delete().deletingChildrenIfNeeded().forPath("/event");
    TimeUnit.SECONDS.sleep(1);
  }
}
