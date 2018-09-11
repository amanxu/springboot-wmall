package com.wmall.service.impl;

import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.wmall.dto.OrderDto;
import com.wmall.service.PayService;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @Author: slyritd <br>
 * @Date: 2017-11-18 14:43 <br>
 */
@Service
public class PayServiceImpl implements PayService {

    @Override
    public void create(OrderDto orderDto) {
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
    }
}
