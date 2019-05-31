package com.cm.inventory.thread;

import com.cm.common.JsonUtils;
import com.cm.inventory.request.Request;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

@Slf4j
public class RequestWorkThread implements Callable<Boolean> {

    private ArrayBlockingQueue<Request> queue;

    RequestWorkThread(ArrayBlockingQueue<Request> q) {
        this.queue = q;
    }

    @Override
    public Boolean call() {
        while (true) {
            Request request = null;
            try {
                request = queue.take();
                request.process();
            } catch (Exception e) {
                log.error("库存工作线程执行任务失败,inventory: {}", JsonUtils.serialize(request));
            }
        }
    }
}
