package com.cm.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ProductInfo {

    private Long productId;
    private String productName;
    private String desc;
    private Date updateTime;

}
