package org.cx.javaapi.lock;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event;
import org.cx.ConnStringConstant;

public class DistributeLock {

  private static final String ROOT_LOCK="/locks"; //根节点
  
  private int sessionTimeout = 5000; //会话超时时间
  
  private ZooKeeper zk;

  private String lockID; //记录锁节点id
  
  private final static byte[] data={1,2}; //节点的数据

  private CountDownLatch countDownLatch=new CountDownLatch(1);
  
  public DistributeLock() {
    try {
      this.zk = new ZooKeeper(ConnStringConstant.CONNECT_STRING, 5000, new Watcher() {
        public void process(WatchedEvent watchedEvent) {
          // 如果当前的连接状态是连接成功的，那么通过计数器去控制
          if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            countDownLatch.countDown();
            System.out.println(watchedEvent.getState());
          }
        }
      });
      countDownLatch.await();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
}
  
  public boolean lock() {
    try {
      lockID = zk.create(ROOT_LOCK+"/", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
      System.out.println(Thread.currentThread().getName()+"-->"+"创建了节点"+lockID+",开始竞争锁");
      
      List<String> childrenNodes = zk.getChildren(ROOT_LOCK, true); //获取根节点下的所有子节点
      
      //排序，从小到大
      SortedSet<String> sortedSet = new TreeSet<String>();
      for (String children : childrenNodes) {
        sortedSet.add(ROOT_LOCK + "/" + children);
      }
      String first=sortedSet.first(); //拿到最小的节点
      
      if (lockID.equals(first)) {
        //表示当前就是最小的节点
        System.out.println(Thread.currentThread().getName()+"->成功获得锁，lock节点为:["+lockID+"]");
        return true;
      }

      //获取比当前节点小的节点
      SortedSet<String> lessThanLockId=sortedSet.headSet(lockID);
      if (!lessThanLockId.isEmpty()) {
        String prevLockID=lessThanLockId.last();//拿到比当前LOCKID这个几点更小的上一个节点
        
        zk.exists(prevLockID,new LockWatcher(countDownLatch));
        
        countDownLatch.await(sessionTimeout, TimeUnit.MILLISECONDS);
        //上面这段代码意味着如果会话超时或者节点被删除（释放）了
        System.out.println(Thread.currentThread().getName()+" 成功获取锁：["+lockID+"]");
        
        return true;
      }
      
    } catch (KeeperException | InterruptedException e) {
      e.printStackTrace();
    }
    
    return false;
  }
  
  
  public boolean unlock() {
    System.out.println(Thread.currentThread().getName()+"-->开始释放锁"+lockID);
    
    try {
      zk.delete(lockID, -1);
      System.out.println("节点["+lockID+"]成功被删除");
      return true;
    } catch (InterruptedException | KeeperException e) {
      e.printStackTrace();
    }
    
    return false;
  }
  
  public static void main(String[] args) {
    final CountDownLatch latch=new CountDownLatch(10);
    Random random = new Random();
    
    for (int i=0; i<10; i++) {
      new Thread(new Runnable() {
        DistributeLock lock=null;
        @Override
        public void run() {
          try {
            lock=new DistributeLock();
            latch.countDown();
            latch.await();
            Thread.sleep(random.nextInt(500));
            lock.lock();
          } catch (InterruptedException e) {
            e.printStackTrace();
          } finally {
            if (lock != null) {
              lock.unlock();
            }
          }
        }
      }).start();
    }
    
  }
}
