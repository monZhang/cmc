package com.cm.inventory.service.Impl;

import com.cm.common.CacheKey;
import com.cm.entity.Inventory;
import com.cm.inventory.mapper.InventoryMapper;
import com.cm.inventory.service.InventoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

    @Resource
    private InventoryMapper inventoryMapper;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public Inventory getInventoryById(Long productId) {
        Inventory inventory = inventoryMapper.getInventoryById(productId);
        log.debug("从db获库存取信息成功:{}", inventory);
        return inventory;
    }

    @Override
    public boolean updateInventory(Inventory inventory) {
        return inventoryMapper.updateInventory(inventory) == 1;
    }

    @Override
    public boolean delete2Cache(Inventory inventory) {
        if (Objects.nonNull(inventory)) {
            return redisTemplate.delete(CacheKey.inventory + inventory.getProductId());
        }
        return false;
    }

    @Override
    public boolean hasKey(Inventory inventory) {
        if (Objects.nonNull(inventory)) {
            return redisTemplate.hasKey(CacheKey.inventory + inventory.getProductId());
        }
        return false;
    }

    @Override
    public Inventory getInventoryFromCache(Long inventoryId) {
        try {
            String jsonObject = redisTemplate.opsForValue().get(CacheKey.inventory + inventoryId);
            if (!StringUtils.isEmpty(jsonObject)) {
                return jsonMapper.readValue(jsonObject, Inventory.class);
            }
        } catch (IOException e) {
            log.error("从redis中获取库存失败,inventoryId: {}", inventoryId, e);
        }
        return null;
    }

    @Override
    public void put2Cache(Inventory inventory) {

        try {
            if (Objects.isNull(inventory)) {
                return;
            }
            redisTemplate.opsForValue().set(CacheKey.inventory + inventory.getProductId(), jsonMapper.writeValueAsString(inventory));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


}
