package com.wmall.wechat.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * @Description:
 * @Author: niexx <br>
 * @Date: 2017-11-23 13:35 <br>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
@XmlType
public class ImgMsgReceive extends BaseMsgPassive implements Serializable {

    @XmlElement(name = "PicUrl")
    private String picUrl;

    @XmlElement(name = "MediaId")
    private String mediaId;

    @XmlElement(name = "MsgId")
    private String msgId;
    @XmlElement(name = "AgentID")
    private Integer agentId;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
