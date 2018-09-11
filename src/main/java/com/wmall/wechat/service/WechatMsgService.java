package com.wmall.wechat.service;

import com.alibaba.fastjson.JSON;
import com.wmall.utils.JsonUtil;
import com.wmall.utils.WeChatUtil;
import com.wmall.utils.XMLUtil;
import com.wmall.wechat.aes.AesException;
import com.wmall.wechat.aes.WXBizMsgCrypt;
import com.wmall.wechat.enums.ChatMsgTypeEnum;
import com.wmall.wechat.pojo.ImgMsgReceive;
import com.wmall.wechat.pojo.TextMsgReceive;
import com.wmall.wechat.pojo.VoiceMsgReceive;
import com.wmall.wechat.websocket.SocketSessionRegistry;
import com.wmall.wechat.websocket.WebOutputMsgDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Set;

/**
 * @Description: 处理微信聊天信息服务
 * @Author: niexx <br>
 * @Date: 2017-11-23 11:29 <br>
 */
@Service
public class WechatMsgService {

    Logger logger = LoggerFactory.getLogger(WechatMsgService.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Autowired
    private WechatAccessService wechatAccessService;

    @Autowired
    private ChatMsgAssemble chatMsgAssemble;

    //session操作类
    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;
    //消息发送工具
    @Autowired
    private SimpMessagingTemplate template;

    /*************企业微信相关参数****************/
    private static int agentId = 1000002; // 企业应用的ID
    private static String corpId = "wwae679b71d1986931"; //企业的ID
    private static String token = "Vk8kt3XcIy25pxbbkPXcXtGTVWm";// 消息加解密的Token
    private static String encodeAesKey = "xcCDGztJxq81a4p5tglI9oWJmL5PR5wic3X7RuqAHyj"; // 消息的EncodingAESKey
    private String sendRandom = null;
    private String sendTimeStamp = null;

    public String verifyURL(String msgSign, String timeStamp, String nonce, String echostr) {
        String echoStr = null;
        try {
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(token, encodeAesKey, corpId);
            echoStr = wxcpt.VerifyURL(msgSign, timeStamp, nonce, echostr);
        } catch (AesException e) {
            e.printStackTrace();
        }
        return echoStr;
    }

    /**
     * 解密并处理接收到的微信消息
     *
     * @param msgSign
     * @param timeStamp
     * @param nonce
     * @param msg
     * @return
     */
    public String receiveMsg(String msgSign, String timeStamp, String nonce, String msg) {
        String sendMsg = null;
        try {
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(token, encodeAesKey, corpId);
            String receiveMsg = wxcpt.DecryptMsg(msgSign, timeStamp, nonce, msg);
            logger.info("After Decrypt Msg:{}", receiveMsg);
            // 解析出明文xml标签的内容进行处理
            String msgType = getMsgType(receiveMsg);
            if (ChatMsgTypeEnum.TEXT.getType().equals(msgType)) {// 文本类型
                TextMsgReceive textMsgReceive = XMLUtil.convertToJavaBean(receiveMsg, TextMsgReceive.class);
                logger.info("当前消息：{},{}", msgType, JsonUtil.toJson(textMsgReceive));
                // 1.持久化微信用户发送的消息
                chatMsgAssemble.persistRecTextMsg(textMsgReceive);
                // 2.通知Web前端消息(文本消息)
                WebOutputMsgDto outputMsgDto = new WebOutputMsgDto();
                outputMsgDto.setAgentId(textMsgReceive.getAgentId());
                outputMsgDto.setUserId(textMsgReceive.getFromUserName());
                outputMsgDto.setChatContent(textMsgReceive.getContent());
                sendMsg = JSON.toJSONString(outputMsgDto);
                //3.通知前端webSocket
                String currentAgentId = textMsgReceive.getAgentId().toString();
                pushMsgToWeb(currentAgentId, outputMsgDto);
            } else if (ChatMsgTypeEnum.IMG.getType().equals(msgType)) { // 图片类型
                ImgMsgReceive imgMsgReceive = XMLUtil.convertToJavaBean(receiveMsg, ImgMsgReceive.class);
                logger.info("当前消息：{},{}", msgType, JsonUtil.toJson(imgMsgReceive));
                // 1.持久化微信用户发送的图片消息,同时同步到本地服务器，imgUrl本地服务器的路径
                String imgUrl = chatMsgAssemble.persistRecImgMsg(imgMsgReceive);
                // 2.通知Web前端消息(图片消息,图片的URL)
                WebOutputMsgDto outputMsgDto = new WebOutputMsgDto();
                outputMsgDto.setAgentId(imgMsgReceive.getAgentId());
                outputMsgDto.setUserId(imgMsgReceive.getFromUserName());
                outputMsgDto.setMediaUrl(imgUrl);
                sendMsg = JSON.toJSONString(outputMsgDto);
                //3.通知前端webSocket
                String currentAgentId = imgMsgReceive.getAgentId().toString();
                pushMsgToWeb(currentAgentId, outputMsgDto);
            } else if (ChatMsgTypeEnum.VOICE.getType().equals(msgType)) { // 语音类型
                VoiceMsgReceive voiceMsgReceive = XMLUtil.convertToJavaBean(receiveMsg, VoiceMsgReceive.class);
                logger.info("当前消息：{},{}", msgType, JsonUtil.toJson(voiceMsgReceive));
                // 1.持久化微信用户发送的语音消息,同时同步到本地服务器，voiceUrl本地服务器的路径
                String voiceUrl = chatMsgAssemble.persistRecVoiceMsg(voiceMsgReceive);
                // 2.通知Web前端消息(语音消息,语音的URL)
                WebOutputMsgDto outputMsgDto = new WebOutputMsgDto();
                outputMsgDto.setAgentId(voiceMsgReceive.getAgentId());
                outputMsgDto.setUserId(voiceMsgReceive.getFromUserName());
                outputMsgDto.setMediaUrl(voiceUrl);
                sendMsg = JSON.toJSONString(outputMsgDto);
                //3.通知前端webSocket
                String currentAgentId = voiceMsgReceive.getAgentId().toString();
                pushMsgToWeb(currentAgentId, outputMsgDto);
            } else if (ChatMsgTypeEnum.EVENT.getType().equals(msgType)) {
                logger.info("事件消息类型:{},{}", msgType, receiveMsg);
            } else {
                // TODO 不支持的消息类型，本系统只支持文字，图片和语音
                logger.info("不支持当前消息类型:{}", msgType);
            }
        } catch (Exception e) {
            logger.info("接收消息，消息处理异常!!!");
            e.printStackTrace();
        }
        return sendMsg;
    }

    /**
     * 根据应用的ID获取对应的session集合，然后逐个推送
     *
     * @param currentAgentId
     * @param outputMsgDto
     */
    private void pushMsgToWeb(String currentAgentId, WebOutputMsgDto outputMsgDto) {
        Set<String> sessionSet = webAgentSessionRegistry.getSessionIds(currentAgentId);
        for (String sessionId : sessionSet) { // 根据当前的应用获取所有已连接上的session
            // 推送消息到前端socket
            template.convertAndSendToUser(sessionId, "/topic/greetings", outputMsgDto, createHeaders(sessionId));
        }
    }

    /**
     * 发送微信消息
     *
     * @param msg
     */
    public String assembleSendMsg(String msg) {
        String encryptMsg = null;
        try {
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(token, encodeAesKey, corpId);
            genRandomAndTime();// 更新当前随机数和时间戳
            encryptMsg = wxcpt.EncryptMsg(msg, sendTimeStamp, sendRandom);
            logger.info("After Encrypt Msg:{}", encryptMsg);
        } catch (AesException e) {
            e.printStackTrace();
        }
        return encryptMsg;
    }

    /**
     * 生成随机数和时间戳
     */
    public void genRandomAndTime() {
        sendTimeStamp = String.valueOf(System.currentTimeMillis());
        sendRandom = String.valueOf(1000000000 + new Random().nextInt(999999999));
    }

    /**
     * 解析报文体,获取消息类型
     *
     * @param msg
     * @return
     */
    public String getMsgType(String msg) {
        String msgType = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(msg);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);
            Element root = document.getDocumentElement();
            NodeList nodeList = root.getElementsByTagName("MsgType");
            if (nodeList.getLength() == 0) {
                throw new Exception("微信消息类型不能为空!!!");
            }
            msgType = nodeList.item(0).getTextContent();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgType;
    }

    public MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    public static void main(String[] args) {
        /*TextMsgReceive textMsgReceive = new TextMsgReceive();
        textMsgReceive.setToUserName("Dcits");
        textMsgReceive.setFromUserName("Niexxiaoxu");
        textMsgReceive.setCreateTime("111111000000");
        textMsgReceive.setMsgType("text");
        textMsgReceive.setContent("我是TEXT类型测试数据");
        textMsgReceive.setMsgId("20171123");
        textMsgReceive.setAgentId("1100");
        String xmlData = XMLUtil.convertToXml(textMsgReceive);
        System.out.println("xmlData=" + xmlData);
        TextMsgReceive textMsgReceiveBean = new TextMsgReceive();
        textMsgReceiveBean = XMLUtil.convertToJavaBean(xmlData, TextMsgReceive.class);
        System.out.println("textMsgReceiveBean=" + textMsgReceiveBean);
        // 发送消息类型转换测试
        ImgMsgPassive imgMsgPassive = new ImgMsgPassive();
        MediaIdPassive mediaIdPassive = new MediaIdPassive();
        mediaIdPassive.setMediaId("10001");
        imgMsgPassive.setCreateTime("1111111111");
        imgMsgPassive.setMediaIdPassive(mediaIdPassive);
        imgMsgPassive.setMsgType("image");
        imgMsgPassive.setToUserName("amanxu");
        imgMsgPassive.setFromUserName("DCITS");
        String sendMsg = XMLUtil.convertToXml(imgMsgPassive);
        System.out.println("SendMsg=" + sendMsg);*/

        // 图片下载
        String uploadFileUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=qpg-6Qvj32X3v4a4u4xxIqhUmvdO_FtgdSdhSdFnWrNeq0xV7dAriu6cHhZE2gEv2QlhrqMX4UgOtGex0Yo20Gb7rsAGFoQUddyNZHqoYZ9AFTkHLyqhLGySNXg5NFcF9lCDpdM6S8MZcIHfbOlkxygNoPwugzpW23DENfPAMuhYL918rg5PhPlWu2c8s53aA5vf62STixPsRtdNXXy86w&media_id=1sI4M9Vb57P-LRH1iUtcGJiWTsf_gHooXgb88GUzuOFD8Oaad7jip03nDuf6YK6AY";
        String saveUrl = "D:\\tempFile\\img_upload" + new Random().nextInt(100) + ".jpg";
        boolean loadResult = WeChatUtil.httpDownLoadFile(uploadFileUrl, saveUrl);
        System.out.println("loadResult=" + loadResult);

        // 语音下载 ID=1TDOG1ZHzeWoxXSljgV2mWBJplNLfzrjtrRiy7X1qFZU
        String uploadVoiceUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=qpg-6Qvj32X3v4a4u4xxIqhUmvdO_FtgdSdhSdFnWrNeq0xV7dAriu6cHhZE2gEv2QlhrqMX4UgOtGex0Yo20Gb7rsAGFoQUddyNZHqoYZ9AFTkHLyqhLGySNXg5NFcF9lCDpdM6S8MZcIHfbOlkxygNoPwugzpW23DENfPAMuhYL918rg5PhPlWu2c8s53aA5vf62STixPsRtdNXXy86w&media_id=1TDOG1ZHzeWoxXSljgV2mWBJplNLfzrjtrRiy7X1qFZU";
        String voiceSaveUrl = "D:\\tempFile\\voice_upload" + new Random().nextInt(100) + ".amr";
        boolean voiceLoadResult = WeChatUtil.httpDownLoadFile(uploadVoiceUrl, voiceSaveUrl);
        System.out.println("voiceLoadResult=" + voiceLoadResult);
    }
}
