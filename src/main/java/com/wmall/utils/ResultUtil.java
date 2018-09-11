package com.wmall.utils;


import com.wmall.commons.MallResult;

public class ResultUtil {

    public static MallResult success(Object object) {
        MallResult result = new MallResult();
        result.setCode("0000");
        result.setMsg("成功");
        result.setData(object);
        return result;
    }

    public static MallResult success() {
        MallResult result = new MallResult();
        result.setCode("0000");
        result.setMsg("成功");
        return result;
    }

    public static MallResult error(String code, String msg) {
        MallResult result = new MallResult();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
