package com.wmall.wechat.pojo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @Description: 主动推送消息返回结果
 * @Author: niexx <br>
 * @Date: 2017-11-23 17:27 <br>
 */
public class ActiveMsgResult implements Serializable {
    private int errcode;
    private String errmsg;
    private String invaliduser;
    private String invalidparty;
    private String invalidtag;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getInvaliduser() {
        return invaliduser;
    }

    public void setInvaliduser(String invaliduser) {
        this.invaliduser = invaliduser;
    }

    public String getInvalidparty() {
        return invalidparty;
    }

    public void setInvalidparty(String invalidparty) {
        this.invalidparty = invalidparty;
    }

    public String getInvalidtag() {
        return invalidtag;
    }

    public void setInvalidtag(String invalidtag) {
        this.invalidtag = invalidtag;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
