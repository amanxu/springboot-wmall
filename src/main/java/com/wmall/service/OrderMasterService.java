package com.wmall.service;

import com.wmall.commons.Page;
import com.wmall.dto.OrderDto;
import com.wmall.entity.OrderMaster;

import java.util.List;

/**
 * Create By niexx
 * 2017-11-12 10:40
 * 订单主表操作接口
 */
public interface OrderMasterService extends BaseService<OrderMaster> {

    Page<OrderDto> findByOpenid(String openid, Page<OrderMaster> page);

    /** 创建订单 */
    OrderDto create(OrderDto orderDto);

    /** 查询订单 */
    OrderDto findByOrderId(String orderId);

    /** 查询订单列表 */
    List<OrderDto> findByOpenid(String openid);

    /** 取消订单 */
    OrderDto cancel(OrderDto orderDto);

    /** 完结订单 */
    OrderDto finish(OrderDto orderDto);

    /** 支付订单 */
    OrderDto paid(OrderDto orderDto);
}
