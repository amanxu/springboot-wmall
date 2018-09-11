package com.wmall.service.impl;

import com.google.common.collect.Lists;
import com.wmall.commons.MallException;
import com.wmall.commons.Page;
import com.wmall.converter.OrderMasterConverterDto;
import com.wmall.dto.CartDto;
import com.wmall.dto.OrderDto;
import com.wmall.entity.OrderDetail;
import com.wmall.entity.OrderMaster;
import com.wmall.entity.ProductInfo;
import com.wmall.enums.OrderStatusEnum;
import com.wmall.enums.PayStatusEnum;
import com.wmall.enums.ResultEnum;
import com.wmall.service.OrderDetailService;
import com.wmall.service.OrderMasterService;
import com.wmall.service.ProductService;
import com.wmall.utils.KeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderMasterServiceImplImpl extends BaseServiceImpl<OrderMaster> implements OrderMasterService {

    private static Logger logger = LoggerFactory.getLogger(OrderMasterServiceImplImpl.class);
    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    public Page<OrderDto> findByOpenid(String openid, Page<OrderMaster> page) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("from OrderMaster where 1=1 and buyerOpenid = ? order by createTime");
        List<Object> paramList = Arrays.asList(openid);
        Page<OrderMaster> orderMasterPage = findPage(page, hqlBuffer.toString(), paramList);
        List<OrderDto> orderDtoList = OrderMasterConverterDto.convert(orderMasterPage.getResult());
        Page<OrderDto> orderDtoPage = new Page<OrderDto>(page.getPageNo(), page.getPageSize(), page.getTotalCount(), orderDtoList);
        return orderDtoPage;
    }

    @Override
    public OrderDto create(OrderDto orderDto) {

        String orderId = KeyUtils.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        // 1. 查询商品（数量，价格）
        for (OrderDetail orderDetail : orderDto.getOrderDetailList()) {
            ProductInfo productInfo = productService.findById(orderDetail.getProductId());
            if (productInfo == null) {
                throw new MallException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            // 2.计算订单总价 注：BigDecimal不能使用* 作为乘法
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);
            //订单详情入库
            orderDetail.setDetailId(KeyUtils.genUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetailService.save(orderDetail);
        }
        //3.写入订单数据库（orderMaster,orderDetail）
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId(orderId);
        BeanUtils.copyProperties(orderDto, orderMaster);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMaster.setOrderAmount(orderAmount);
        save(orderMaster);
        //4.减库存
        List<CartDto> cartDtoList = Lists.newArrayList();
        orderDto.getOrderDetailList().stream().map(e -> new CartDto(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.decreaseStock(cartDtoList);

        return orderDto;
    }

    @Override
    public OrderDto findByOrderId(String orderId) {
        OrderMaster orderMaster = fetch("orderId", orderId);
        if (orderMaster == null) {
            throw new MallException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailService.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new MallException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster, orderDto);
        orderDto.setOrderDetailList(orderDetailList);
        return null;
    }

    @Override
    public List<OrderDto> findByOpenid(String openid) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("from OrderMaster where buyerOpenid=?");
        List<Object> paramList = Arrays.asList(openid);
        List<OrderMaster> orderMasterList = findByHql(hqlBuffer.toString(), paramList);

        List<OrderDto> orderDtoList = OrderMasterConverterDto.convert(orderMasterList);
        return orderDtoList;
    }

    @Override
    @Transactional
    public OrderDto cancel(OrderDto orderDto) {
        OrderMaster orderMaster = new OrderMaster();
        // 判断订单状态
        if (!OrderStatusEnum.NEW.getCode().equals(orderDto.getOrderStatus())) {
            logger.info("【取消订单】订单状态不正确，OrderId={},OrderStatus={}", orderDto.getOrderId(), orderDto.getOrderStatus());
            throw new MallException(ResultEnum.ORDER_STATUS_ERROR);
        }
        // 修改订单状态
        orderDto.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDto, orderMaster);
        saveOrUpdate(orderMaster);
        // 返回库存
        if (CollectionUtils.isEmpty(orderDto.getOrderDetailList())) {
            logger.info("【取消订单】订单中午商品详情，orderDto={}", orderDto);
            throw new MallException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDto> cartDtoList = orderDto.getOrderDetailList().stream()
                .map(e -> new CartDto(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDtoList);
        // 如果已支付,需要退款
        if (PayStatusEnum.SUCCESS.equals(orderDto.getPayStatus())) {
            // TODO
        }
        return orderDto;
    }

    @Override
    public OrderDto finish(OrderDto orderDto) {
        // 判断订单状态
        if (!OrderStatusEnum.NEW.getCode().equals(orderDto.getOrderStatus())) {
            logger.info("【完结订单】订单状态不正确，orderId={},orderStatus={}");
            throw new MallException(ResultEnum.ORDER_STATUS_ERROR);
        }
        // 修改订单状态
        orderDto.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDto, orderMaster);
        saveOrUpdate(orderMaster);
        return orderDto;
    }

    @Override
    public OrderDto paid(OrderDto orderDto) {
        // 判断订单状态
        if (!OrderStatusEnum.NEW.getCode().equals(orderDto.getOrderStatus())) {
            logger.info("【订单支付成功】订单状态不正确，orderId={},orderStatus={}");
            throw new MallException(ResultEnum.ORDER_STATUS_ERROR);
        }
        // 判断支付状态
        if (!PayStatusEnum.WAIT.getCode().equals(orderDto.getOrderStatus())) {
            logger.info("【订单支付完成】订单状态不正确，orderDto={}", orderDto);
        }
        // 修改支付状态
        orderDto.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDto, orderMaster);
        saveOrUpdate(orderMaster);
        return orderDto;
    }
}
