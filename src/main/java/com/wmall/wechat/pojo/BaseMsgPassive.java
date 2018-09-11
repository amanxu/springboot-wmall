package com.wmall.wechat.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.xml.bind.annotation.*;

/**
 * @Description:
 * @Author: niexx <br>
 * @Date: 2017-11-23 13:41 <br>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class BaseMsgPassive {

    @XmlElement(name = "ToUserName")
    private String toUserName;
    @XmlElement(name = "FromUserName")
    private String fromUserName;
    @XmlElement(name = "CreateTime")
    private String createTime;
    @XmlElement(name = "MsgType")
    private String msgType;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
