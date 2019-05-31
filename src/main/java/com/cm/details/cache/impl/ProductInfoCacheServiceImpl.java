package com.cm.details.cache.impl;

import com.cm.common.CacheKey;
import com.cm.common.JsonUtils;
import com.cm.details.cache.AbstractCacheService;
import com.cm.entity.ProductInfo;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("productInfoCacheServiceImpl")
public class ProductInfoCacheServiceImpl extends AbstractCacheService<ProductInfo> {

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    private static final String local = "local";

    @Override
    @CachePut(key = "'local_product_' + #productInfo.productId", cacheNames = local)
    public ProductInfo put2LocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    @Override
    protected void put2Redis(ProductInfo productInfo) {
        redisTemplate.opsForValue().set(CacheKey.productInfo, JsonUtils.serialize(productInfo));
    }

    @Override
    @Cacheable(key = "'local_product_' + #productId", cacheNames = local)
    public ProductInfo getFromLocalCache(Long productId) {
        return null;
    }

    @Override
    protected ProductInfo getFromRedis(Long productId) {
        String productInfoStr = redisTemplate.opsForValue().get(CacheKey.productInfo + productId);
        return JsonUtils.parse(productInfoStr, ProductInfo.class);
    }

    @Override
    protected ProductInfo pullInfo(Long id) {
        return null;
    }

    @Override
    protected boolean isNewDate(ProductInfo newData, ProductInfo redisData) {
        return newData.getUpdateTime().after(redisData.getUpdateTime());
    }
}
