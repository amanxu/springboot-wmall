package com.wmall.wechat.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * @Description:
 * @Author: niexx <br>
 * @Date: 2017-11-23 14:21 <br>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
@XmlType
public class TextMsgPassive extends BaseMsgPassive implements Serializable {

    @XmlElement(name = "Content")
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
