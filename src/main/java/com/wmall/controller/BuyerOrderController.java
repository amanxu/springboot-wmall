package com.wmall.controller;

import com.wmall.commons.MallException;
import com.wmall.commons.MallResult;
import com.wmall.commons.Page;
import com.wmall.converter.OrderFormConverterDto;
import com.wmall.dto.OrderDto;
import com.wmall.entity.OrderMaster;
import com.wmall.enums.ResultEnum;
import com.wmall.form.OrderForm;
import com.wmall.service.CustomerService;
import com.wmall.service.OrderMasterService;
import com.wmall.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private CustomerService customerService;

    // 创建订单
    @RequestMapping("/create")
    public MallResult<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确，orderForm={}", orderForm);
            throw new MallException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDto orderDto = OrderFormConverterDto.convert(orderForm);
        orderMasterService.create(orderDto);
        if (CollectionUtils.isEmpty(orderDto.getOrderDetailList())) {
            log.info("【创建订单】购物车不能为空");
            throw new MallException(ResultEnum.CART_EMPTY);
        }
        OrderDto createResult = orderMasterService.create(orderDto);
        Map<String, String> map = new HashMap<>();
        map.put("orderId", createResult.getOrderId());

        return ResultUtil.success(map);
    }

    // 订单列表
    @RequestMapping("/list") // http://127.0.0.1:9000/wmall/customer/order/list?openid=amanxu&page=1&size=5
    public MallResult<List<OrderDto>> list(@RequestParam("openid") String openid, @RequestParam("page") Integer pageNo, @RequestParam("size") Integer pageSize) {
        if (StringUtils.isEmpty(openid)) {
            log.error("【查询订单列表】openid为空");
            throw new MallException(ResultEnum.PARAM_ERROR);
        }
        Page<OrderMaster> page = new Page(pageNo, pageSize);
        Page<OrderDto> orderDtoPage = orderMasterService.findByOpenid(openid, page);
        return ResultUtil.success(orderDtoPage);
    }

    // 订单详情
    @RequestMapping("/detail")
    public MallResult<OrderDto> detail(@RequestParam("openid") String openid, @RequestParam("orderId") String orderId) {
        OrderDto orderDto = customerService.findOrderOne(openid, orderId);
        return ResultUtil.success(orderDto);
    }

    // 取消订单
    @RequestMapping("/cancel")
    public MallResult cancel(@RequestParam("openid") String openid, @RequestParam("orderId") String orderId) {
        customerService.cancelOrder(openid, orderId);
        return ResultUtil.success();
    }

}
