package com.cm.queue;

import com.cm.common.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
public class ReloadCacheQueue<T> {

    private ArrayBlockingQueue<T> reloadCacheTask = new ArrayBlockingQueue<>(1000);

    private ReloadCacheQueue() {

    }

    /**
     * 加入任务队列
     *
     * @param t
     */
    public void put2ReloadCacheTaskQueue(T t) {
        try {
            if (Objects.nonNull(t)) {
                reloadCacheTask.put(t);
            }
        } catch (InterruptedException e) {
            log.error("添加reloadCacheTask失败，参数：{}", JsonUtils.serialize(t), e);
        }
    }

    public T takeReloadCacheTask() {
        try {
            return reloadCacheTask.take();
        } catch (InterruptedException e) {
            log.error("获取reloadCacheTask失败.", e);
        }
        return null;
    }


    /**
     * 初始化queue
     */
    public static void InitReloadCacheQueue() {
        getInstance();
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static ReloadCacheQueue getInstance() {
        return Instance.getInstance();
    }

    private static class Instance {
        private static ReloadCacheQueue reloadCacheQueue;

        static {
            reloadCacheQueue = new ReloadCacheQueue();
        }

        private static ReloadCacheQueue getInstance() {
            return reloadCacheQueue;
        }
    }

}