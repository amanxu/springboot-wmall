package com.wmall.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Author: niexx <br>
 * @Date: 2017-11-24 9:18 <br>
 */
@Entity
@Table(name = "wechat_info")
public class WechatInfo implements Serializable {

    private String chatId;// 聊天信息ID
    private String corpId;// 企业ID
    private Integer agentId; // 企业应用的ID
    private String userId;// 应用内用户ID
    private Integer msgOwn;// 消息归属（1:用户/2:企业）
    private Integer msgType;// 消息类型（1：文本,2：语音,3：图片）
    private String chatContent;// 文本消息内容
    private String mediaUrl;// 图片/语音素材在服务器的存放路径
    private Integer readState;// 消息阅读状态（1：未读/2：已读）
    private Date createTime;// 消息创建时间（消息按时间降序排列）
    private Date updateTime;// 消息更新时间（消息查阅时间）

    @Id
    @Column(name = "CHAT_ID", unique = true)
    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @Column(name = "CORP_ID", length = 64)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "AGENT_ID")
    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    @Column(name = "USER_ID", length = 64)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "MSG_OWN", length = 2)
    public Integer getMsgOwn() {
        return msgOwn;
    }

    public void setMsgOwn(Integer msgOwn) {
        this.msgOwn = msgOwn;
    }

    @Column(name = "MSG_TYPE", length = 2)
    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    @Column(name = "CHAT_CONTENT", length = 1024)
    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    @Column(name = "MEDIA_URL")
    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    @Column(name = "READ_STATE", length = 2)
    public Integer getReadState() {
        return readState;
    }

    public void setReadState(Integer readState) {
        this.readState = readState;
    }

    @Column(name = "CREATE_TIME", length = 32)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_TIME", length = 32)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
