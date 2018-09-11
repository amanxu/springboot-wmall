package com.wmall.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "product_info")
public class ProductInfo implements Serializable {

    /**
     * 产品ID
     */
    @Id
    @Column(name = "PRODUCT_ID", unique = true)
    private String productId;

    /**
     * 产品名称
     */
    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_PRICE")
    private BigDecimal productPrice;

    @Column(name = "PRODUCT_STOCK")
    private Integer productStock; //库存

    @Column(name = "PRODUCT_ICON")
    private String productIcon; // 商品小图

    @Column(name = "PRODUCT_STATUS")
    private Integer productStatus;// 状态0：正常 1：下架

    @Column(name = "CATEGORY_TYPE")
    private Integer categoryType;

    /**
     * 产品描述
     */
    @Column(name = "PRODUCT_DESC")
    private String productDesc;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }

    public String getProductIcon() {
        return productIcon;
    }

    public void setProductIcon(String productIcon) {
        this.productIcon = productIcon;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}