package com.wmall.entity;

import com.wmall.enums.OrderStatusEnum;
import com.wmall.enums.PayStatusEnum;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单主表
 */
@Entity
@Table(name = "order_master")
public class OrderMaster implements Serializable {

    private String orderId;

    private String buyerName;

    private String buyerPhone;

    private String buyerAddress;

    /**
     * 微信 openId
     */
    private String buyerOpenid;

    private BigDecimal orderAmount;
    /**
     * 订单状态, 默认为0 新订单
     */
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();

    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    private Date createTime;

    private Date updateTime;

    @Id
    @Column(name = "ORDER_ID", unique = true)
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Column(name = "BUYER_NAME", length = 32)
    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    @Column(name = "BUYER_PHONE", length = 16)
    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    @Column(name = "BUYER_ADDRESS")
    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    @Column(name = "BUYER_OPENID")
    public String getBuyerOpenid() {
        return buyerOpenid;
    }

    public void setBuyerOpenid(String buyerOpenid) {
        this.buyerOpenid = buyerOpenid;
    }

    @Column(name = "ORDER_AMOUNT", length = 12)
    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    @Column(name = "ORDER_STATUS", length = 2)
    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Column(name = "PAY_STATUS", length = 2)
    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", length = 32)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME", length = 32)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
