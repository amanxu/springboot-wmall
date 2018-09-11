package com.wmall.wechat.service;

import com.alibaba.fastjson.JSON;
import com.wmall.config.WechatInfoConfig;
import com.wmall.entity.WechatInfo;
import com.wmall.service.WechatInfoService;
import com.wmall.utils.WeChatUtil;
import com.wmall.wechat.enums.ChatInfoOwnEnum;
import com.wmall.wechat.enums.ChatInfoReadStatueEnum;
import com.wmall.wechat.enums.ChatMsgTypeEnum;
import com.wmall.wechat.enums.WeChatConstant;
import com.wmall.wechat.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @Description:
 * @Author: niexx <br>
 * @Date: 2017-11-24 11:03 <br>
 */
@Service
public class ChatMsgAssemble {

    Logger logger = LoggerFactory.getLogger(ChatMsgAssemble.class);
    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 精确到毫秒

    @Autowired
    private WechatInfoService wechatInfoService;

    @Autowired
    private WechatInfoConfig wechatInfoConfig;

    @Autowired
    private WechatAccessService wechatAccessService;

    /**
     * 持久化接收到的微信用户的文本消息
     *
     * @param textMsgReceive
     */
    public void persistRecTextMsg(TextMsgReceive textMsgReceive) {
        WechatInfo wechatInfo = new WechatInfo();
        wechatInfo.setChatContent(textMsgReceive.getContent());
        wechatInfo.setAgentId(textMsgReceive.getAgentId());
        wechatInfo.setUserId(textMsgReceive.getFromUserName());
        wechatInfo.setMsgOwn(ChatInfoOwnEnum.CUSTOMER.getCode());// 接收的微信消息归属用户
        wechatInfo.setMsgType(ChatMsgTypeEnum.TEXT.getCode());
        wechatInfo.setCreateTime(new Date());
        wechatInfo.setCorpId(wechatInfoConfig.getCorpId());
        wechatInfo.setReadState(ChatInfoReadStatueEnum.UNREAD.getCode());
        wechatInfo.setChatId(genChatId("TXT"));// 数据库记录的ID（String类型）
        wechatInfoService.saveOrUpdate(wechatInfo);
    }

    /**
     * 持久化接收到的微信用户的图片消息
     *
     * @param imgMsgReceive
     */
    public String persistRecImgMsg(ImgMsgReceive imgMsgReceive) throws Exception {
        WechatInfo wechatInfo = new WechatInfo();
        wechatInfo.setChatId(genChatId("IMG"));
        wechatInfo.setMsgType(ChatMsgTypeEnum.IMG.getCode());
        wechatInfo.setMsgOwn(ChatInfoOwnEnum.CUSTOMER.getCode());
        wechatInfo.setUserId(imgMsgReceive.getFromUserName());
        wechatInfo.setAgentId(imgMsgReceive.getAgentId());
        wechatInfo.setCorpId(wechatInfoConfig.getCorpId());
        wechatInfo.setReadState(ChatInfoReadStatueEnum.UNREAD.getCode());
        wechatInfo.setCreateTime(new Date());
        // 1.获取用户发送图片的mediaId；2.同步临时文件到本地服务器 3.保存文件路径到数据库URL字段中
        String mediaId = imgMsgReceive.getMediaId();
        String savePath = wechatInfoConfig.getFileSavePath() + "\\img\\" + mediaId + ".jpg";
        String downLoadUrl = wechatAccessService.genDownLoadUrl(WeChatConstant.corpToken, mediaId);
        boolean downLoadResult = WeChatUtil.httpDownLoadFile(downLoadUrl, savePath);
        if (!downLoadResult) {
            throw new Exception("从微信服务器下载图片失败!!!");
        }
        wechatInfo.setMediaUrl(savePath);
        wechatInfoService.saveOrUpdate(wechatInfo);
        return savePath;
    }

    /**
     * 持久化接收到的微信用户的语音消息
     *
     * @param voiceMsgReceive
     * @throws Exception
     */
    public String persistRecVoiceMsg(VoiceMsgReceive voiceMsgReceive) throws Exception {
        WechatInfo wechatInfo = new WechatInfo();
        wechatInfo.setChatId(genChatId("VOI"));
        wechatInfo.setReadState(ChatInfoReadStatueEnum.UNREAD.getCode());
        wechatInfo.setMsgType(ChatMsgTypeEnum.VOICE.getCode());
        wechatInfo.setAgentId(voiceMsgReceive.getAgentId());
        wechatInfo.setCorpId(wechatInfoConfig.getCorpId());
        wechatInfo.setUserId(voiceMsgReceive.getFromUserName());
        wechatInfo.setMsgOwn(ChatInfoOwnEnum.CUSTOMER.getCode());
        wechatInfo.setCreateTime(new Date());
        // 1.获取用户发送图片的mediaId；2.同步临时文件到本地服务器 3.保存文件路径到数据库URL字段中
        String mediaId = voiceMsgReceive.getMediaId();
        String savePath = wechatInfoConfig.getFileSavePath() + "\\voice\\" + mediaId + ".amr";
        String downLoadUrl = wechatAccessService.genDownLoadUrl(WeChatConstant.corpToken, mediaId);
        boolean downLoadResult = WeChatUtil.httpDownLoadFile(downLoadUrl, savePath);
        if (!downLoadResult) {
            throw new Exception("从微信服务器下载语音失败!!!");
        }
        wechatInfo.setMediaUrl(savePath);
        wechatInfoService.saveOrUpdate(wechatInfo);
        return savePath;
    }

    /**
     * 组装返回给微信用户TEXT文档
     *
     * @param toUser
     * @param content
     * @return
     */
    public String assembleSendTextMsg(String toUser, Integer agentId, String content) {
        // 返回文本类型的消息
        TextMsgActive textMsgActive = new TextMsgActive();
        textMsgActive.setTouser(toUser);
        textMsgActive.setMsgtype(ChatMsgTypeEnum.TEXT.getType());
        textMsgActive.setAgentid(agentId);
        textMsgActive.setSafe(0);
        TextActive textActive = new TextActive();
        textActive.setContent(content);
        textMsgActive.setText(textActive);
        return JSON.toJSONString(textMsgActive);
    }

    /**
     * 组装返回给微信用户IMG消息,同时上传图片到微信服务器
     *
     * @param toUser
     * @param file
     * @return
     */
    public String assembleSendImgMsg(String toUser, Integer agentId, File file) {
        String uploadResult = WeChatUtil.httpRequest(wechatAccessService.genUploadUrl(WeChatConstant.corpToken, ChatMsgTypeEnum.IMG.getType()), file);
        UploadFileResult uploadFileResult = JSON.parseObject(uploadResult, UploadFileResult.class);
        ImgMsgActive imgMsgActive = new ImgMsgActive();
        MediaActive mediaActive = new MediaActive();
        mediaActive.setMedia_id(uploadFileResult.getMedia_id());// 上传图片的ID
        imgMsgActive.setAgentid(agentId);
        imgMsgActive.setMsgtype(ChatMsgTypeEnum.IMG.getType());
        imgMsgActive.setTouser(toUser);
        imgMsgActive.setSafe(0);
        imgMsgActive.setImage(mediaActive);
        return JSON.toJSONString(imgMsgActive);
    }

    /**
     * 组装返回给微信用户VOICE消息，同时上传语音到微信服务器
     *
     * @param toUser
     * @param file
     * @return
     */
    public String assembleSendVoiceMsg(String toUser, Integer agentId, File file) {
        String uploadResult = WeChatUtil.httpRequest(wechatAccessService.genUploadUrl(WeChatConstant.corpToken, ChatMsgTypeEnum.IMG.getType()), file);
        UploadFileResult uploadFileResult = JSON.parseObject(uploadResult, UploadFileResult.class);
        VoiceMsgActive voiceMsgActive = new VoiceMsgActive();
        MediaActive mediaActive = new MediaActive();
        mediaActive.setMedia_id(uploadFileResult.getMedia_id());// 上传语音的ID
        voiceMsgActive.setVoice(mediaActive);
        voiceMsgActive.setAgentid(agentId);
        voiceMsgActive.setMsgtype(ChatMsgTypeEnum.VOICE.getType());
        voiceMsgActive.setTouser(toUser);
        return JSON.toJSONString(voiceMsgActive);
    }

    /**
     * 生成聊天记录的ID
     *
     * @param prefix 消息类型前缀
     *               文本：TXT 图片：IMG 语音：VOI
     * @return
     */
    public String genChatId(String prefix) {
        StringBuffer chatIdBuf = new StringBuffer();
        chatIdBuf.append(prefix);
        int random = 1000000 + new Random().nextInt(999999);
        chatIdBuf.append(random);
        chatIdBuf.append(df.format(new Date()));
        return chatIdBuf.toString();
    }

    public static void main(String[] args) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        System.out.println(df.format(new Date()));
    }

}
