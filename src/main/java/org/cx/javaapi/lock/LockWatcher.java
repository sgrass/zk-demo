package org.cx.javaapi.lock;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class LockWatcher implements Watcher {

  private CountDownLatch latch;

  public LockWatcher(CountDownLatch latch) {
    this.latch = latch;
  }

  @Override
  public void process(WatchedEvent event) {
    if (event.getType() == Event.EventType.NodeDeleted) {
      latch.countDown();
    }
  }

}
