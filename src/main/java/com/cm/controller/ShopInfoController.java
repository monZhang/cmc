package com.cm.controller;

import com.cm.details.cache.CacheService;
import com.cm.entity.ShopInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/shop_info")
public class ShopInfoController {

    @Resource
    @Qualifier("shopInfoCacheServiceImpl")
    private CacheService<ShopInfo> cacheService;

    @GetMapping("/{shopId:\\d+}")
    public ShopInfo getShopInfo(@PathVariable Long shopId) {
        return cacheService.getFromCache(shopId);
    }

}
