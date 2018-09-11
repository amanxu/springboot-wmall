package com.wmall.wechat.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * @Description:
 * @Author: niexx <br>
 * @Date: 2017-11-23 14:23 <br>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
@XmlType
public class ImgMsgPassive extends BaseMsgPassive implements Serializable {

    @XmlElement(name = "Image")
    private MediaIdPassive mediaIdPassive;

    public MediaIdPassive getMediaIdPassive() {
        return mediaIdPassive;
    }

    public void setMediaIdPassive(MediaIdPassive mediaIdPassive) {
        this.mediaIdPassive = mediaIdPassive;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
