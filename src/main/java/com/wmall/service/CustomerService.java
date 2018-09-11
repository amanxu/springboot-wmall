package com.wmall.service;


import com.wmall.dto.OrderDto;

/**
 * 买家
 * Created by 廖师兄
 * 2017-06-22 00:11
 */
public interface CustomerService {

    /**
     * 查询一个订单
     *
     * @param openid
     * @param orderId
     * @return
     */
    OrderDto findOrderOne(String openid, String orderId);

    /**
     * 取消订单
     *
     * @param openid
     * @param orderId
     * @return
     */
    OrderDto cancelOrder(String openid, String orderId);
}
