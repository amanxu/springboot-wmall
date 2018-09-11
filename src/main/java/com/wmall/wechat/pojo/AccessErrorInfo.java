package com.wmall.wechat.pojo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

@Slf4j
public class AccessErrorInfo implements Serializable {

    /**
     * 错误码
     */
    private String errcode;
    /**
     * 错误描述
     */
    private String errmsg;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
