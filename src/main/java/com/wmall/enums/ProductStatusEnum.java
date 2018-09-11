package com.wmall.enums;

public enum ProductStatusEnum {
    UP(0, "在售"),
    DOWN(1, "下架");
    private Integer code;
    private String msg;

    ProductStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsgByCode(Integer code) {
        for (ProductStatusEnum statusEnum : ProductStatusEnum.values()) {
            if (code == statusEnum.code) {
                return statusEnum.msg;
            }
        }
        return null;
    }
}
