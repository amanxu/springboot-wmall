package com.wmall.wechat.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @Description: 主动推送消息之语音消息
 * @Author: niexx <br>
 * @Date: 2017-11-23 17:19 <br>
 */
public class VoiceMsgActive extends BaseMsgActive implements Serializable{
    private MediaActive voice;

    public MediaActive getVoice() {
        return voice;
    }

    public void setVoice(MediaActive voice) {
        this.voice = voice;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
