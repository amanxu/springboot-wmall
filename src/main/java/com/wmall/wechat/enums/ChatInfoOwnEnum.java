package com.wmall.wechat.enums;

/**
 * @Description: 聊天信息归属
 * @Author: niexx <br>
 * @Date: 2017-11-24 9:48 <br>
 */
public enum ChatInfoOwnEnum {

    CUSTOMER(1, "用户"),
    CORP(2, "企业");

    private int code;
    private String msg;

    ChatInfoOwnEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsgByCode(int code) {
        for (ChatInfoOwnEnum infoOwnEnum : ChatInfoOwnEnum.values()) {
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
