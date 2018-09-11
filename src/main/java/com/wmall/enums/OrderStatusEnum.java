package com.wmall.enums;

public enum OrderStatusEnum {
    NEW(0, "新订单"),
    FINISHED(1, "完成"),
    CANCEL(2, "已取消");
    private Integer code;
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
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
        for (OrderStatusEnum statusEnum : OrderStatusEnum.values()) {
            if (code == statusEnum.code) {
                return statusEnum.msg;
            }
        }

        return null;
    }
}
