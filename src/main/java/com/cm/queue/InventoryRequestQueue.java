package com.cm.queue;

import com.cm.inventory.request.Request;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 库存相关请求队列管理
 */
public class InventoryRequestQueue {

    private List<ArrayBlockingQueue<Request>> queues = Lists.newArrayList();
    /**
     * 标识位map, 用于标记此是否处理过相应的请求:
     * true: 处理过读请求.
     * false: 处理过些请求.
     */
    private Map<Long, Boolean> flagMap = new ConcurrentHashMap<>();

    /**
     * 根据索引获取工作队列
     *
     * @param index
     * @return
     */
    public ArrayBlockingQueue<Request> getQueue(int index) {
        return queues.get(index);
    }

    /**
     * 获取所有工作队列
     *
     * @return
     */
    public List<ArrayBlockingQueue<Request>> getQueues() {
        return queues;
    }

    /**
     * 向工作队列集合添加工作队列
     *
     * @param queue
     */
    public void addQueue(ArrayBlockingQueue<Request> queue) {
        this.queues.add(queue);
    }

    /**
     * 标记map存入
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean putFlagMap(Long key, Boolean value) {
        return flagMap.put(key, value);
    }

    /**
     * 是否被标记
     *
     * @param key
     * @return
     */
    public Boolean isFlag(Long key) {
        return flagMap.get(key);
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static InventoryRequestQueue getInstance() {
        return Instance.getInstance();
    }

    private static class Instance {
        private static InventoryRequestQueue inventoryRequestQueue;

        static {
            inventoryRequestQueue = new InventoryRequestQueue();
        }

        private static InventoryRequestQueue getInstance() {
            return inventoryRequestQueue;
        }
    }

    private InventoryRequestQueue() {

    }
}
