package com.wmall.wechat.websocket;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @Description: webSocket传入参数
 * @Author: niexx <br>
 * @Date: 2017-11-26 13:13 <br>
 */
public class WebInputMsgDto implements Serializable {

    private Integer agentId; // 企业应用的ID
    private String userId;// 应用内用户ID
    private String chatContent;// 文本消息内容

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
