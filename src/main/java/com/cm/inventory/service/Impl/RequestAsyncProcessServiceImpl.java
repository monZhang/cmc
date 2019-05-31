package com.cm.inventory.service.Impl;

import com.cm.inventory.request.RefreshInventory2Cache;
import com.cm.inventory.request.Request;
import com.cm.inventory.request.UpdateInventory2DB;
import com.cm.inventory.service.RequestAsyncProcessService;
import com.cm.queue.InventoryRequestQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
@Service
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {

    /**
     * 处理请求
     *
     * @param request
     * @throws InterruptedException
     */
    public void process(Request request) throws InterruptedException {

        //根据productId路由(hash)得到具体的队列
        InventoryRequestQueue inventoryRequestQueue = InventoryRequestQueue.getInstance();
        int index = this.route(request.getProductId());
        ArrayBlockingQueue<Request> requestQueue = inventoryRequestQueue.getQueue(index);

        //如果是强制刷新请求直接处理
        if (request.isForceRefresh()) {
            requestQueue.put(request);
            return;
        }
        //非强制刷新请求进行去重处理
        if (request instanceof UpdateInventory2DB) {
            // 如果是一个更新数据库的请求，那么就将那个productId对应的标识设置为true
            inventoryRequestQueue.putFlagMap(request.getProductId(), true);
        } else if (request instanceof RefreshInventory2Cache) {
            Boolean flag = inventoryRequestQueue.isFlag(request.getProductId());

            // 如果flag是null, 说明之前没有处理过读请求标记为false
            // flag = true,说明之前处理过写请求,设置为false代表处理过读请求
            if (flag == null || flag) {
                inventoryRequestQueue.putFlagMap(request.getProductId(), false);
            }
            // 如果是缓存刷新的请求，而且发现标识不为空，但是标识是false
            // 说明前面已经有一个读请求了
            if (flag != null && !flag) {
                // 对于这种读请求，直接就过滤掉，不要放到后面的内存队列里面去了
                log.debug("库存读请求被去重,不加入任务队列,productId:{}", request.getProductId());
                return;
            }
        }
        requestQueue.put(request);
    }

    /**
     * 根据id hash路由到指定的queue
     *
     * @param productId
     * @return
     */
    private int route(Long productId) {
        int h;
        int hash = (productId == null) ? 0 : (h = productId.hashCode()) ^ (h >>> 16);
        return (InventoryRequestQueue.getInstance().getQueues().size() - 1) & hash;
    }
}
