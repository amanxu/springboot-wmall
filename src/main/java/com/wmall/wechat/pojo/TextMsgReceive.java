package com.wmall.wechat.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * @Description: 接收TEXT消息
 * @Author: niexx <br>
 * @Date: 2017-11-23 12:58 <br>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
@XmlType
public class TextMsgReceive extends BaseMsgPassive implements Serializable {

    @XmlElement(name = "Content")
    private String content;
    @XmlElement(name = "MsgId")
    private String msgId;
    @XmlElement(name = "AgentID")
    private Integer agentId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
