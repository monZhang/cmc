package com.cm.inventory.request;

public interface Request {

    /**
     * 获取id,用于路由队列
     *
     * @return
     */
    long getProductId();

    /**
     * 请求处理方法
     *
     * @return
     */
    boolean process();

    /**
     * 是否强制刷新库存缓存,
     * 不强制刷新: 如果队列中有相应商品请求时,新请求不加入队列直接抛弃.
     *
     * @return
     */
    boolean isForceRefresh();

}
