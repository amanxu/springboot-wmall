package com.wmall.converter;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wmall.commons.MallException;
import com.wmall.dto.OrderDto;
import com.wmall.entity.OrderDetail;
import com.wmall.enums.ResultEnum;
import com.wmall.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class OrderFormConverterDto {

    private static Gson gson = new Gson();

    public static OrderDto convert(OrderForm orderForm) {
        OrderDto orderDto = new OrderDto();
        orderDto.setBuyerName(orderForm.getName());
        orderDto.setBuyerPhone(orderForm.getPhone());
        orderDto.setBuyerAddress(orderForm.getAddress());
        orderDto.setBuyerOpenid(orderForm.getOpenid());
        List<OrderDetail> orderDetailList = Lists.newArrayList();
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>() {
            }.getType());
        } catch (Exception e) {
            log.error("【对象转换】错误，String={}", orderForm.getItems());
            throw new MallException(ResultEnum.PARAM_ERROR);
        }
        orderDto.setOrderDetailList(orderDetailList);
        return orderDto;
    }
}
