package com.wmall.wechat.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @Description: 主动推送之媒体ID
 * @Author: niexx <br>
 * @Date: 2017-11-23 17:13 <br>
 */
public class MediaActive implements Serializable {
    private String media_id;

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
