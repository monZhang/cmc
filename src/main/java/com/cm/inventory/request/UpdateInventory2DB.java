package com.cm.inventory.request;

import com.cm.entity.Inventory;
import com.cm.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateInventory2DB implements Request {

    private Inventory inventory;
    private InventoryService inventoryService;

    public UpdateInventory2DB(Inventory inventory, InventoryService inventoryService) {
        this.inventory = inventory;
        this.inventoryService = inventoryService;
    }

    /**
     * 刪除緩存并更新db
     *
     * @return
     */
    @Override
    public boolean process() {
        inventoryService.delete2Cache(inventory);
        if (inventoryService.hasKey(inventory)) {
            log.error("删除Redis库存缓存失败,inventory: {}", inventory);
            return false;
        }
        log.debug("删除Redis库存缓存成功,等待更新数据库数据,inventory: {}", inventory);
        boolean updateResult = inventoryService.updateInventory(inventory);
        log.debug("删除Redis库存缓存后,更新数据库数据,result: {}", updateResult);
        return updateResult;
    }

    @Override
    public long getProductId() {
        return inventory.getProductId();
    }

    @Override
    public boolean isForceRefresh() {
        return false;
    }
}
