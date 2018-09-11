package com.wmall.wechat.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @Description:
 * @Author: niexx <br>
 * @Date: 2017-11-23 14:25 <br>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class MediaIdPassive {

    @XmlElement(name = "MediaId")
    private String mediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
