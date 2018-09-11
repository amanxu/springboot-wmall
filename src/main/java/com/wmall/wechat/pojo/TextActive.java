package com.wmall.wechat.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @Description: 主动消息文本类 text 内容对象
 * @Author: niexx <br>
 * @Date: 2017-11-23 17:05 <br>
 */
public class TextActive implements Serializable {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
