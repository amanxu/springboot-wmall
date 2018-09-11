package com.wmall.service;

import com.wmall.entity.OrderDetail;

import java.util.List;

/**
 * 订单详情
 * Create By niexx
 */
public interface OrderDetailService extends BaseService<OrderDetail> {

    List<OrderDetail> findByOrderId(String orderId);
}

