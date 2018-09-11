package com.wmall.wechat.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @Description: 主动推送消息基类
 * @Author: niexx <br>
 * @Date: 2017-11-23 16:58 <br>
 */
public class BaseMsgActive {
    //成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向该企业应用的全部成员发送
    private String touser;
    //部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
    private String toparty;
    //标签ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
    private String totag;
    //消息类型
    private String msgtype;
    //企业应用的id，整型。可在应用的设置页面查看
    private int agentid;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getToparty() {
        return toparty;
    }

    public void setToparty(String toparty) {
        this.toparty = toparty;
    }

    public String getTotag() {
        return totag;
    }

    public void setTotag(String totag) {
        this.totag = totag;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public int getAgentid() {
        return agentid;
    }

    public void setAgentid(int agentid) {
        this.agentid = agentid;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
