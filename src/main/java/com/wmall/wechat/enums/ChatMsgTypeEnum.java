package com.wmall.wechat.enums;

/**
 * @Description: 企业微信消息类型
 * @Author: niexx <br>
 * @Date: 2017-11-23 12:51 <br>
 */
public enum ChatMsgTypeEnum {
    TEXT(10, "text", "文本"),
    IMG(11, "image", "图片"),
    VOICE(12, "voice", "语音"),//语音消息
    VIDEO(13, "video", "视频"),//视频消息
    LOCATION(14, "location", "位置"),//位置消息
    LINK(15, "link", "链接"),//链接消息
    EVENT(16, "event", "事件");//事件类型
    private int code;
    private String type;
    private String msg;

    ChatMsgTypeEnum(int code, String type, String msg) {
        this.code = code;
        this.type = type;
        this.msg = msg;
    }

    public String getMsgByCode(int code) {
        for (ChatMsgTypeEnum msgTypeEnum : ChatMsgTypeEnum.values()) {
            if (msgTypeEnum.code == code) {
                return msgTypeEnum.msg;
            }
        }
        return null;
    }

    public String getTypeByCode(int code) {
        for (ChatMsgTypeEnum msgTypeEnum : ChatMsgTypeEnum.values()) {
            if (msgTypeEnum.code == code) {
                return msgTypeEnum.type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
