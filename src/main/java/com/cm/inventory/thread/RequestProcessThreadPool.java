package com.cm.inventory.thread;

import com.cm.inventory.request.Request;
import com.cm.queue.InventoryRequestQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class RequestProcessThreadPool {

    /**
     * 初始化线程池、工作队列
     */
    private RequestProcessThreadPool() {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        InventoryRequestQueue inventoryRequestQueues = InventoryRequestQueue.getInstance();
        for (int i = 0; i < 10; i++) {
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<>(1000);
            inventoryRequestQueues.addQueue(queue);
            threadPool.submit(new RequestWorkThread(queue));
        }
        log.debug("========初始化线程池、工作队列完成==========");
    }

    /**
     * 初始化线程池,队列
     */
    public static void init() {
        getInstance();
    }

    /**
     * 获取请求处理线程池
     *
     * @return
     */
    public static RequestProcessThreadPool getInstance() {
        return Instance.getInstance();
    }

    private static class Instance {
        private static RequestProcessThreadPool threadPool;

        static {
            threadPool = new RequestProcessThreadPool();
        }

        private static RequestProcessThreadPool getInstance() {
            return threadPool;
        }
    }

}
