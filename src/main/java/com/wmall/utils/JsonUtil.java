package com.wmall.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wmall.dto.OrderDto;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Description: JSON 格式化工具
 *
 * @Author: slyritd <br>
 * @Date: 2017-11-18 15:01 <br>
 */
public class JsonUtil {

    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }

    public void baba(String[] args) {
        OrderDto orderDto = new OrderDto();
        orderDto.setBuyerAddress("上海");
        orderDto.setBuyerName("slyritd");
        orderDto.setBuyerPhone("18888888888");
        String jsonStr = JsonUtil.toJson(orderDto);
        Gson gson = new Gson();
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Collection<Object> values = jsonObject.values();
    }
}
