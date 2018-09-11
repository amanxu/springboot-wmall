package com.wmall.enums;

public enum PayStatusEnum {

    WAIT(0, "等待支付"),
    SUCCESS(1, "支付成功");

    private Integer code;
    private String msg;

    PayStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgByCode(Integer code) {
        for (PayStatusEnum statusEnum : PayStatusEnum.values()) {
            if (code == statusEnum.code) {
                return statusEnum.msg;
            }
        }
        return null;
    }
}
