package com.wmall.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class KeyUtils {
    Logger logger = LoggerFactory.getLogger(KeyUtils.class);

    /**
     * 生成唯一主键
     *
     * @return
     */
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
