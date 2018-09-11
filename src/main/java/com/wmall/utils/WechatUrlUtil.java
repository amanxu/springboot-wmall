package com.wmall.utils;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.util.http.URIUtil;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class WechatUrlUtil {

    public static String genCodeUrl(String fullUrl, String appId, String redirectUrl, String scope, String state) {
        // https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
        fullUrl = fullUrl.replace("APPID", appId)
                .replace("REDIRECT_URI", URIUtil.encodeURIComponent(redirectUrl))
                .replace("SCOPE", scope)
                .replace("STATE", StringUtils.trimToEmpty(state));
        log.info("genCodeUrl={}", fullUrl);
        return fullUrl;
    }

    public static String genTokenUrl(String fullUrl, String appId, String secret, String code) {
        //https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        fullUrl = fullUrl.replace("APPID", appId)
                .replace("SECRET", secret)
                .replace("CODE", code);
        log.info("genTokenUrl={}", fullUrl);
        return fullUrl;
    }

    public static String genUserInfoUrl(String fullUrl, String token, String openid) {
        //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
        fullUrl = fullUrl.replace("ACCESS_TOKEN", token)
                .replace("OPENID", openid);
        log.info("genUserInfoUrl={}", fullUrl);
        return fullUrl;
    }

    public static String genRefreshTokenUrl(String fullUrl, String appId, String freshToken) {
        //https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
        fullUrl = fullUrl.replace("APPID", appId)
                .replace("REFRESH_TOKEN", freshToken);
        return fullUrl;
    }

    public static String genValidTokenUrl(String fullUrl, String accessToken, String openid) {
        //https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID
        fullUrl = fullUrl.replace("ACCESS_TOKEN", accessToken)
                .replace("OPENID", openid);
        return fullUrl;
    }


    public static void main(String[] args) {
        WechatUrlUtil wechatUrlUtil = new WechatUrlUtil();
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
        String result = wechatUrlUtil.genCodeUrl(url, "000000000000", "11111111111111", "22222222222", "000000000000");
        System.out.println("result=" + result);
    }
}
