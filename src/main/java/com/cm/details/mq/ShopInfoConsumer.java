package com.cm.details.mq;

import com.cm.common.JsonUtils;
import com.cm.details.cache.CacheService;
import com.cm.entity.ShopInfo;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 店铺信息修改MQ消费
 */
@Component
@RabbitListener(queues = "shop_info_queue", containerFactory = "rabbitListenerContainerFactory")
public class ShopInfoConsumer {

    @Resource
    @Qualifier("shopInfoCacheServiceImpl")
    private CacheService<ShopInfo> cacheService;

    @RabbitHandler
    public void process(byte[] msgByte) {
        Long shopInfoId = JsonUtils.parse(msgByte, Long.class);
        cacheService.reloadCache(shopInfoId);
    }

}
