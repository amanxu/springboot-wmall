package com.wmall.wechat.websocket;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: webSocket 响应参数
 * @Author: niexx <br>
 * @Date: 2017-11-26 13:13 <br>
 */
public class WebOutputMsgDto implements Serializable {

    private String chatId;// 聊天信息ID
    private Integer agentId; // 企业应用的ID
    private String userId;// 应用内用户ID
    private Integer msgOwn;// 消息归属（1:用户/2:企业）
    private Integer msgType;// 消息类型（1：文本,2：语音,3：图片）
    private String chatContent;// 文本消息内容
    private String mediaUrl;// 图片/语音素材在服务器的存放路径
    private Integer readState;// 消息阅读状态（1：未读/2：已读）
    private Date createTime;// 消息创建时间（消息按时间降序排列）
    private Date updateTime;// 消息更新时间（消息查阅时间）
    private boolean newUser;// 是否是新增的用户

    public WebOutputMsgDto(String chatContent) {
        this.chatContent = chatContent;
    }

    public WebOutputMsgDto() {
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

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

    public Integer getMsgOwn() {
        return msgOwn;
    }

    public void setMsgOwn(Integer msgOwn) {
        this.msgOwn = msgOwn;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Integer getReadState() {
        return readState;
    }

    public void setReadState(Integer readState) {
        this.readState = readState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
