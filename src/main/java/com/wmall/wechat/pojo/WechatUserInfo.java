package com.wmall.wechat.pojo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * 微信用户信息
 * create by niexx
 * 2017-11-14 22:19
 */
@Data
public class WechatUserInfo implements Serializable {

    /**
     * 微信唯一标识openid
     */
    private String openid;

    /**
     * 微信昵称
     */
    private String nickName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;
    /**
     * 国家
     */
    private String country;

    /**
     * 用户头像URL
     */
    private String headimgurl;
    /**
     * 用户特权信息，json数组
     */
    private JSONArray privilege;

    /**
     * 只有在用户将公众号绑定在微信开放平台账号后才会出现该字段
     */
    private String unionid;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
