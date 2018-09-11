package com.wmall.service.impl;

import com.wmall.entity.OrderDetail;
import com.wmall.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderDetailServiceImplImpl extends BaseServiceImpl<OrderDetail> implements OrderDetailService {
    @Override
    public List<OrderDetail> findByOrderId(String orderId) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("from OrderDetail where orderId = ?");
        List<Object> paramList = Arrays.asList(orderId);
        return super.findByHql(hqlBuffer.toString(), paramList);
    }
}
