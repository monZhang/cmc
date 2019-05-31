package com.cm.inventory.service;

import com.cm.inventory.request.Request;

public interface RequestAsyncProcessService {

    /**
     * 异步的请求处理方法
     *
     * @param request
     * @throws InterruptedException
     */
    void process(Request request) throws InterruptedException;

}
