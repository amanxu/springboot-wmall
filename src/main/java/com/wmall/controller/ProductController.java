package com.wmall.controller;

import com.google.common.collect.Lists;
import com.wmall.commons.MallResult;
import com.wmall.entity.ProductCategory;
import com.wmall.entity.ProductInfo;
import com.wmall.service.ProductCategoryService;
import com.wmall.service.ProductService;
import com.wmall.utils.ResultUtil;
import com.wmall.vo.ProductInfoVo;
import com.wmall.vo.ProductVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping("/add")
    public void productAdd(HttpServletRequest request) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductDesc("河南特色烩面");
        productInfo.setProductName("烩面");
        productInfo.setProductId("1");
        productService.save(productInfo);
    }

    @RequestMapping("/list")
    public MallResult productList() {
        // 1.查询所有上架商品
        List<ProductInfo> productInfoList = productService.findAllProduct();

        // 2.查询类目
        List<Integer> categoryTypeList = Lists.newArrayList();
        // 传统方式
        /*for (ProductInfo productInfo : productInfoList) {
            categoryTypeList.add(productInfo.getCategoryType());
        }*/
        // 精简方式（Java8 lambda） (有待研究)
        categoryTypeList = productInfoList.stream().map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        List<ProductCategory> categoryList = productCategoryService.findByCategoryType(categoryTypeList);
        // 3.数据拼装
        List<ProductVo> productVoList = Lists.newArrayList();
        for (ProductCategory productCategory : categoryList) {
            ProductVo productVo = new ProductVo();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());
            List<ProductInfoVo> productInfoVoList = Lists.newArrayList();
            for (ProductInfo productInfo : productInfoList) {
                if (productCategory.getCategoryType() == productInfo.getCategoryType()) {
                    ProductInfoVo productInfoVo = new ProductInfoVo();
                    BeanUtils.copyProperties(productInfo, productInfoVo);// Bean复制
                    productInfoVoList.add(productInfoVo);
                }
                productVo.setProductInfoVoList(productInfoVoList);
                productVoList.add(productVo);
            }
        }
        return ResultUtil.success(productVoList);
    }


}
