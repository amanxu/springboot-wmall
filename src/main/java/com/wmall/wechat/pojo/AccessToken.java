package com.wmall.wechat.pojo;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

@Data
public class AccessToken implements Serializable {

    private String access_token;
    private String expires_in; // 过期时间
    private String refresh_token;// 用户刷新accessToken
    private String openid; // 用户唯一标识
    private String scope; // 用户授权的作用域，使用逗号(,)分隔

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
