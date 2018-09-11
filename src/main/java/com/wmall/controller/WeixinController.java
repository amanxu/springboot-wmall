package com.wmall.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * slyritd.mynatapp.cc
 * 测试账号信息：
 * appID
 * wx7220dc9281a9518e
 * appsecret
 * d4624c36b6795d1d99dcf0547af5443d
 */
@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {

    @RequestMapping("/wxAuth")
    public void wxAuth(@RequestParam("code") String code) {
        log.info("进入微信认证方法...");
        log.info("微信授权，微信认证Code={}", code);

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx7220dc9281a9518e&secret=d4624c36b6795d1d99dcf0547af5443d&code=" + code + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        log.info("response={}", response);
    }
}
