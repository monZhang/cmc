package com.cm.inventory.request;

import com.cm.entity.Inventory;
import com.cm.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RefreshInventory2Cache implements Request {

    private boolean isForceRefresh;
    private long productId;
    private InventoryService inventoryService;

    public RefreshInventory2Cache(Long productId, InventoryService inventoryService, boolean isForceRefresh) {
        this.productId = productId;
        this.inventoryService = inventoryService;
        this.isForceRefresh = isForceRefresh;
    }

    /**
     * 从db中读取商品信息保存到cache中
     *
     * @return
     */
    @Override
    public boolean process() {
        Inventory product = inventoryService.getInventoryById(productId);
        inventoryService.put2Cache(product);
        log.debug("从db中获取数据并加入缓存.");
        return true;
    }

    @Override
    public long getProductId() {
        return productId;
    }

    @Override
    public boolean isForceRefresh() {
        return isForceRefresh;
    }
}
