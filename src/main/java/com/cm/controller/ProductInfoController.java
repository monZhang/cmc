package com.cm.controller;

import com.cm.details.cache.CacheService;
import com.cm.entity.ProductInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/product_info")
public class ProductInfoController {

    @Resource
    @Qualifier("productInfoCacheServiceImpl")
    private CacheService<ProductInfo> cacheService;

    @GetMapping("/{productId:\\d+}")
    public ProductInfo getProductInfo(@PathVariable Long productId) {
        return cacheService.getFromCache(productId);
    }

}
