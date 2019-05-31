package com.cm.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class LockUtils {

    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final String cmcLock = "/cmc_lock_";
    private ZooKeeper zooKeeper;

    /**
     * 加锁
     *
     * @return
     */
    public boolean acquire(Long id) {
        try {
            zooKeeper.create(cmcLock + id, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            log.debug("获取锁成功，id: {}", id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            int count = 0;
            do {
                try {
                    Thread.sleep(200);
                    zooKeeper.create(cmcLock + id, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                    return true;
                } catch (Exception ex) {
                    count++;
                    if (count > 9) {
                        log.error("id：{}的请求第十次获取锁失败,不在重试", id);
                        return false;
                    }
                    log.debug("id：{}的请求第{}尝试获取锁", id, count);
                }
            } while (true);
        }
    }

    /**
     * 移除锁
     *
     * @return
     */
    public boolean release(Long id) {
        try {
            zooKeeper.delete(cmcLock + id, 1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("释放锁失败，参数：{}", id);
        }
        return false;
    }

    /**
     * 初始化
     */
    public static void init() {
        getInstance();
    }

    private LockUtils() {
        try {
            zooKeeper = new ZooKeeper("192.168.96.87:2181", 3000, new myWatcher());
            countDownLatch.await();
            log.debug("连接zookeeper成功。。。");
        } catch (Exception e) {
            log.error("连接zookeeper失败！");
            e.printStackTrace();
        }
    }

    private class myWatcher implements Watcher {
        @Override
        public void process(WatchedEvent watchedEvent) {
            if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                countDownLatch.countDown();
            } else {
                log.error("连接zookeeper server失败，keeperState: {}", watchedEvent.getState());
            }
        }
    }

    public static LockUtils getInstance() {
        return Instance.getInstance();
    }

    private static class Instance {
        private static LockUtils lockUtils;

        static {
            lockUtils = new LockUtils();
        }

        private static LockUtils getInstance() {
            return lockUtils;
        }
    }
}
