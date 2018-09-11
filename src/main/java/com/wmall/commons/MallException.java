package com.wmall.commons;

import com.wmall.enums.ResultEnum;

public class MallException extends RuntimeException {

    private Integer code;

    public MallException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public MallException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
