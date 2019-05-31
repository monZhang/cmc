package com.cm.entity;

import lombok.Data;

@Data
public class Inventory {

    /**
     * 商品id
     */
    private Long productId;
    /**
     * 库存
     */
    private Integer inventory;

}