package com.wmall.dto;

import lombok.Data;

/**
 * 购物车
 * Created by 廖师兄
 * 2017-06-11 19:37
 */
@Data
public class CartDto {

    /**
     * 商品Id.
     */
    private String productId;

    /**
     * 数量.
     */
    private Integer productQuantity;

    public CartDto(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
