package com.wmall.controller;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Description:
 * @Author: niexx <br>
 * @Date: 2017-11-23 10:59 <br>
 */
public class BaseController {

    public String readJs(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            BufferedReader bufferedReader = request.getReader();
           /* BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(request.getInputStream(), "UTF-8"));*/
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public byte[] readBytes(HttpServletRequest request) {
        ServletInputStream servletInputStream = null;
        byte[] req = null;
        try {
            servletInputStream = request.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = servletInputStream.read(b)) > 0) {
                out.write(b, 0, i);
            }
            req = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return req;
    }

    public void sendJs(HttpServletResponse response, String js) {
        // 必须写上这句，中文编码问题
        response.setContentType("text/plain;charset=UTF-8");
        // response.setContentType("application/json;charset=utf-8");
        try {
            response.getWriter().write(js);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBytes(HttpServletResponse response, byte[] data) {
        ServletOutputStream servletOutputStream = null;
        try {
            servletOutputStream = response.getOutputStream();
            servletOutputStream.write(data);
            servletOutputStream.flush();
            servletOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
