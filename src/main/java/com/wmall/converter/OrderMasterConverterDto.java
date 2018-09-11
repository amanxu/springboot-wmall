package com.wmall.converter;

import com.wmall.dto.OrderDto;
import com.wmall.entity.OrderMaster;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * OrderMaster 转成 DTO(数据传输对象)
 */
public class OrderMasterConverterDto {

    public static OrderDto convert(OrderMaster orderMaster) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster, orderDto);
        return orderDto;
    }

    public static List<OrderDto> convert(List<OrderMaster> orderMasterList) {
        // lamda 表达式
        return orderMasterList.stream().map(e -> convert(e)).collect(Collectors.toList());
    }
}
