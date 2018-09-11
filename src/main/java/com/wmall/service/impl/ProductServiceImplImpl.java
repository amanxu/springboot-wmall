package com.wmall.service.impl;

import com.google.common.collect.Lists;
import com.wmall.commons.MallException;
import com.wmall.dto.CartDto;
import com.wmall.entity.ProductInfo;
import com.wmall.enums.ResultEnum;
import com.wmall.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductServiceImplImpl extends BaseServiceImpl<ProductInfo> implements ProductService {
    @Override
    public ProductInfo findById(String productId) {
        return super.fetch("productId", productId);
    }

    @Override
    public List<ProductInfo> findAllProduct() {
        return findAll();
    }

    @Override
    public List<ProductInfo> findByStatus(Integer status) {
        String hql = "from ProductInfo where productStatus=?";
        List<Object> paramList = Arrays.asList(status);

        return super.findBySql(hql, paramList);
    }

    @Override
    public List<ProductInfo> findByName(String name) {
        String hql = "from ProductInfo where productName like '%" + name + "%'";
        List<Object> paramList = Lists.newArrayList();
        return super.findBySql(hql, paramList);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDto> cartDtoList) {
        for (CartDto cartDto : cartDtoList) {
            ProductInfo productInfo = findById(cartDto.getProductId());
            if (productInfo == null) {
                throw new MallException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = productInfo.getProductStock() + cartDto.getProductQuantity();
            productInfo.setProductStock(result);
            saveOrUpdate(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDto> cartDtoList) {
        for (CartDto cartDto : cartDtoList) {
            ProductInfo productInfo = findById(cartDto.getProductId());
            if (productInfo == null) {
                throw new MallException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = productInfo.getProductStock() - cartDto.getProductQuantity();
            if (result < 0) {
                throw new MallException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(result);
            save(productInfo);
        }
    }

}
