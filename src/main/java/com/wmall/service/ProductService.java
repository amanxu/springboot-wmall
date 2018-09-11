package com.wmall.service;

import com.wmall.dto.CartDto;
import com.wmall.entity.ProductInfo;

import java.util.List;

public interface ProductService extends BaseService<ProductInfo> {

    public ProductInfo findById(String productId);

    public List<ProductInfo> findAllProduct();

    public List<ProductInfo> findByStatus(Integer status);

    public List<ProductInfo> findByName(String name);

    // 加库存
    public void increaseStock(List<CartDto> cartDtoList);

    // 减库存
    public void decreaseStock(List<CartDto> cartDtoList);

}
