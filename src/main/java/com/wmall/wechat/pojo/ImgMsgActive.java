package com.wmall.wechat.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @Description: 主动推送之图片消息
 * @Author: niexx <br>
 * @Date: 2017-11-23 17:12 <br>
 */
public class ImgMsgActive extends BaseMsgActive implements Serializable {
    private MediaActive image;
    private int safe;

    public MediaActive getImage() {
        return image;
    }

    public void setImage(MediaActive image) {
        this.image = image;
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
