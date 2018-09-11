package com.wmall.service.impl;

import com.wmall.commons.MallException;
import com.wmall.dto.OrderDto;
import com.wmall.enums.ResultEnum;
import com.wmall.service.CustomerService;
import com.wmall.service.OrderMasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private OrderMasterService orderMasterService;

    @Override
    public OrderDto findOrderOne(String openid, String orderId) {
        return checkOrderOwner(openid, orderId);
    }

    @Override
    public OrderDto cancelOrder(String openid, String orderId) {
        OrderDto orderDTO = checkOrderOwner(openid, orderId);
        if (orderDTO == null) {
            log.error("【取消订单】查不到改订单, orderId={}", orderId);
            throw new MallException(ResultEnum.ORDER_NOT_EXIST);
        }

        return orderMasterService.cancel(orderDTO);
    }

    private OrderDto checkOrderOwner(String openid, String orderId) {
        OrderDto orderDto = orderMasterService.findByOrderId(orderId);
        if (orderDto == null) {
            return null;
        }
        //判断是否是自己的订单
        if (!orderDto.getBuyerOpenid().equalsIgnoreCase(openid)) {
            log.error("【查询订单】订单的openid不一致. openid={}, orderDTO={}", openid, orderDto);
            throw new MallException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDto;
    }
}
