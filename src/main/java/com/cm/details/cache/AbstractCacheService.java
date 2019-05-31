package com.cm.details.cache;

import com.cm.common.LockUtils;

import java.util.Objects;

public abstract class AbstractCacheService<T> implements CacheService<T> {

    @Override
    public void reloadCache(Long id) {
        T t = this.pullInfo(id);
        this.put2AllCache(t);
    }

    /**
     * 保存到本地缓存和redis缓存
     *
     * @param t
     */
    private void put2AllCache(T t) {
        this.put2LocalCache(t);
        this.put2Redis(t);
    }

    @Override
    public T getFromCache(Long id) {
        //redis中获取
        T resultFromRedis = this.getFromRedis(id);
        if (Objects.nonNull(resultFromRedis)) {
            return resultFromRedis;
        }
        //本地内存获取,如果存在则存入redis后返回.
        T resultFromLocal = this.getFromLocalCache(id);
        if (Objects.nonNull(resultFromLocal)) {
            this.put2Redis(resultFromLocal);
            return resultFromLocal;
        }
        //远程拉取并更新
        T remoteInfo = this.pullInfo(id);
        //获取分布式锁
        if (Objects.nonNull(remoteInfo) && LockUtils.getInstance().acquire(id)) {
            T infoFromRedis = this.getFromRedis(id);
            //redis中存在则乐观锁校验
            if (Objects.isNull(infoFromRedis) || !this.isNewDate(remoteInfo, infoFromRedis)) {
                return null;
            }
            this.put2AllCache(remoteInfo);
            //释放锁
            LockUtils.getInstance().release(id);
        }
        return remoteInfo;
    }


    /**
     * 保存到本地缓存
     *
     * @param t
     * @return
     */
    protected abstract T put2LocalCache(T t);

    /**
     * 保存到redis中
     *
     * @param t
     */
    protected abstract void put2Redis(T t);

    /**
     * 从本地缓存获取
     *
     * @param productId
     * @return
     */
    protected abstract T getFromLocalCache(Long productId);

    /**
     * 从redis缓存获取
     *
     * @param productId
     * @return
     */
    protected abstract T getFromRedis(Long productId);

    /**
     * 拉取信息
     *
     * @param id
     * @return
     */
    protected abstract T pullInfo(Long id);

    /**
     * 判断新拉取数据是否是新数据
     *
     * @param newData
     * @param redisData
     * @return
     */
    protected abstract boolean isNewDate(T newData, T redisData);

}
