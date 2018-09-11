package com.wmall.wechat.enums;

/**
 * @Description: 聊天信息查阅状态
 * @Author: niexx <br>
 * @Date: 2017-11-24 9:52 <br>
 */
public enum ChatInfoReadStatueEnum {
    UNREAD(1, "未读"),
    READ(2, "已读");

    private int code;
    private String msg;

    ChatInfoReadStatueEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsgByCode(int code) {
        for (ChatInfoReadStatueEnum infoOwnEnum : ChatInfoReadStatueEnum.values()) {
            if (infoOwnEnum.code == code) {
                return infoOwnEnum.msg;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
