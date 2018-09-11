package com.wmall.wechat.websocket;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: niexx <br>
 * @Date: 2017-11-27 16:34 <br>
 */
public class ChatUserInfo implements Serializable {

    private Integer agentId;
    private String userId;
    private Integer unreadCount;// 未读消息记录
    private List<WebOutputMsgDto> outputMsgDtoList;// 所有未读消息

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

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public List<WebOutputMsgDto> getOutputMsgDtoList() {
        return outputMsgDtoList;
    }

    public void setOutputMsgDtoList(List<WebOutputMsgDto> outputMsgDtoList) {
        this.outputMsgDtoList = outputMsgDtoList;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
