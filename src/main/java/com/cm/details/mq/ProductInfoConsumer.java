package com.cm.details.mq;

import com.cm.common.JsonUtils;
import com.cm.details.cache.CacheService;
import com.cm.entity.ProductInfo;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 商品信息修改MQ消费
 */
@Component
@RabbitListener(queues = "product_info_queue", containerFactory = "rabbitListenerContainerFactory")
public class ProductInfoConsumer {

    @Resource
    @Qualifier("productInfoCacheServiceImpl")
    private CacheService<ProductInfo> cacheService;

    @RabbitHandler
    public void process(byte[] msgByte) {
        Long productId = JsonUtils.parse(msgByte, Long.class);
        cacheService.reloadCache(productId);
    }

}
