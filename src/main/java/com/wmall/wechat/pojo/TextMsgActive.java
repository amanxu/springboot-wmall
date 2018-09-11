package com.wmall.wechat.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @Description: 主动推送消息 文本类
 * @Author: niexx <br>
 * @Date: 2017-11-23 16:56 <br>
 */
public class TextMsgActive extends BaseMsgActive implements Serializable {

    private TextActive text;
    private int safe;

    public TextActive getText() {
        return text;
    }

    public void setText(TextActive text) {
        this.text = text;
    }

    public int getSafe() {
        return safe;
    }

    public void setSafe(int safe) {
        this.safe = safe;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
