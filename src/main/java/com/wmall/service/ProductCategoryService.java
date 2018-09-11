package com.wmall.service;

import com.wmall.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService extends BaseService<ProductCategory> {

    public ProductCategory findById(Integer id);

    public List<ProductCategory> findByType(Integer type);

    public List<ProductCategory> findAllCategories();

    public List<ProductCategory> findByCategoryType(List<Integer> typeList);

}
