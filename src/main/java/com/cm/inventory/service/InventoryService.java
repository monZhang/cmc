package com.cm.inventory.service;

import com.cm.entity.Inventory;

public interface InventoryService {

    Inventory getInventoryById(Long inventoryId);

    boolean updateInventory(Inventory inventory);

    boolean delete2Cache(Inventory inventory);

    boolean hasKey(Inventory inventory);

    Inventory getInventoryFromCache(Long inventoryId);

    void put2Cache(Inventory inventory);
}
