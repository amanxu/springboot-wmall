package com.wmall.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * @Description:
 * @Author: niexx <br>
 * @Date: 2017-11-22 20:00 <br>
 */
public class URIUtil {
    private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";

    public URIUtil() {
    }

    public static String encodeURIComponent(String input) {
        if (StringUtils.isEmpty(input)) {
            return input;
        } else {
            int l = input.length();
            StringBuilder o = new StringBuilder(l * 3);

            try {
                for (int i = 0; i < l; ++i) {
                    String e = input.substring(i, i + 1);
                    if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()".indexOf(e) == -1) {
                        byte[] b = e.getBytes("utf-8");
                        o.append(getHex(b));
                    } else {
                        o.append(e);
                    }
                }

                return o.toString();
            } catch (UnsupportedEncodingException var6) {
                var6.printStackTrace();
                return input;
            }
        }
    }

    private static String getHex(byte[] buf) {
        StringBuilder o = new StringBuilder(buf.length * 3);

        for (int i = 0; i < buf.length; ++i) {
            int n = buf[i] & 255;
            o.append("%");
            if (n < 16) {
                o.append("0");
            }

            o.append(Long.toString((long) n, 16).toUpperCase());
        }

        return o.toString();
    }
}
