package com.wmall.controller;

import com.wmall.service.OrderMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description:  <br>
 * @Author: slyritd <br>
 * @Date: 2017-11-18 14:38 <br>
 */
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderMasterService orderMasterService;


}
