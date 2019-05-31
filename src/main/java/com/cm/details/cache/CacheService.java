package com.cm.details.cache;

public interface CacheService<T> {

    /**
     * 存入缓存(本地,redis等)
     *
     * @param id
     */
    void reloadCache(Long id);

    /**
     * 从缓存中获取(如果缓存中不存在则拉取后存入缓存并返回)
     *
     * @param id
     * @return
     */
    T getFromCache(Long id);

}
