package com.wmall.service.impl;

import com.google.common.collect.Lists;
import com.wmall.service.ProductCategoryService;
import com.wmall.entity.ProductCategory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class ProductCategoryServiceImplImpl extends BaseServiceImpl<ProductCategory> implements ProductCategoryService {

    @Override
    public ProductCategory findById(Integer id) {

        ProductCategory productCategory = super.fetch("categoryId", id);
        return productCategory;
    }

    @Override
    public List<ProductCategory> findByType(Integer type) {
        List<ProductCategory> categoryList = Lists.newArrayList();
        String hql = "from ProductCategory where categoryType=?";
        List<Object> paramList = Arrays.asList(type);
        categoryList = super.findByHql(hql, paramList);
        return categoryList;
    }

    @Override
    public List<ProductCategory> findAllCategories() {
        return super.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryType(List<Integer> typeList) {
        List<ProductCategory> categoryList = new ArrayList<ProductCategory>();
        if (typeList != null) {
            for (Integer type : typeList) {
                String hql = "from " + ProductCategory.class.getSimpleName() + " where categoryType=?";
                List<Object> paramList = Lists.newArrayList();
                paramList.add(type);
                List<ProductCategory> categories = this.findByHql(hql, paramList);
                categoryList.addAll(categories);
            }
        }
        return categoryList;
    }
}
