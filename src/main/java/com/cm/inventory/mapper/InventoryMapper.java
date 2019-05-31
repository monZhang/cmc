package com.cm.inventory.mapper;

import com.cm.entity.Inventory;
import org.apache.ibatis.annotations.Param;

public interface InventoryMapper {

    Inventory getInventoryById(@Param("productId") Long productId);

    int updateInventory(Inventory product);

}
