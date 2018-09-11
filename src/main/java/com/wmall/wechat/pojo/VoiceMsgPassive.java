package com.wmall.wechat.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.xml.bind.annotation.*;

/**
 * @Description:
 * @Author: niexx <br>
 * @Date: 2017-11-23 14:29 <br>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
@XmlType
public class VoiceMsgPassive {

    @XmlElement(name = "Voice")
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
