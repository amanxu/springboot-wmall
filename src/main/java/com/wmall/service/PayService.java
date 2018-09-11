package com.wmall.service;

import com.wmall.dto.OrderDto;

/**
 * Description: 支付接口
 *
 * @Author: slyritd <br>
 * @Date: 2017-11-18 14:42 <br>
 */
public interface PayService {

    public void create(OrderDto orderDto);
}
