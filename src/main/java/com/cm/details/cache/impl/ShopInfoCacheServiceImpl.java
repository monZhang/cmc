package com.cm.details.cache.impl;

import com.cm.common.CacheKey;
import com.cm.common.JsonUtils;
import com.cm.details.cache.AbstractCacheService;
import com.cm.entity.ShopInfo;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("shopInfoCacheServiceImpl")
public class ShopInfoCacheServiceImpl extends AbstractCacheService<ShopInfo> {

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    private static final String local = "local";

    @Override
    @CachePut(key = "'local_shop_' + #shopInfo.shopId", cacheNames = local)
    public ShopInfo put2LocalCache(ShopInfo shopInfo) {
        return shopInfo;
    }

    @Override
    @Cacheable(key = "'local_shop_' + #shopId", cacheNames = local)
    public ShopInfo getFromLocalCache(Long shopId) {
        return null;
    }

    @Override
    protected void put2Redis(ShopInfo shopInfo) {
        redisTemplate.opsForValue().set(CacheKey.shopInfo, JsonUtils.serialize(shopInfo));
    }

    @Override
    protected ShopInfo getFromRedis(Long shopId) {
        String shopInfoStr = redisTemplate.opsForValue().get(CacheKey.shopInfo + shopId);
        return JsonUtils.parse(shopInfoStr, ShopInfo.class);
    }

    @Override
    protected ShopInfo pullInfo(Long id) {
        return null;
    }

    @Override
    protected boolean isNewDate(ShopInfo newData, ShopInfo redisData) {
        return newData.getUpdateTime().after(redisData.getUpdateTime());
    }

}
