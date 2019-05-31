package com.cm.common;

import lombok.Data;

@Data
public class CacheKey {

    //库存key
    public static final String inventory = "inventory:";

    public static final String productInfo = "productInfo:";

    public static final String shopInfo = "shopInfo:";

    public static final String localProduct = "local_product_";

    public static final String localShop = "local_shop_";

}
