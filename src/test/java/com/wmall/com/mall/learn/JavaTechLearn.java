package com.wmall.com.mall.learn;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class JavaTechLearn {
    public static void main(String[] args) throws ParseException {
        String a = (String) null;
        log.info(a);

        String dateStr = "15:30";
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        log.info("==Date==:{}", df.parse(dateStr));

        Set<String> setJs = Sets.newHashSet();
        setJs.add("2");
        setJs.add("5");
        setJs.add("7");
        log.info("===SetToStr==={}", setJs.toString());

        Long obj = null;
        log.info("==null != 0  {}", obj != 0);

    }
}
