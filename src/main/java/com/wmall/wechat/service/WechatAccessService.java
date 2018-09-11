package com.wmall.wechat.service;

import com.alibaba.fastjson.JSON;
import com.wmall.config.WechatInfoConfig;
import com.wmall.utils.HttpUtil;
import com.wmall.wechat.enums.WeChatConstant;
import com.wmall.wechat.pojo.*;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.util.http.URIUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 访问微信服务器服务
 * create by niexx
 * 2017-11-14 22:42
 */
@Slf4j
@Service
public class WechatAccessService {

    @Autowired
    private WechatInfoConfig wechatInfoConfig;

    /**
     * 获取用户信息
     *
     * @param token
     * @param openid
     * @return
     */
    public WechatUserInfo queryUserInfo(String openid, String token) {
        String userInfoUrl = String.format(genUserInfoUrl(token, openid));
        String userInfoJson = HttpUtil.httpGetJs(userInfoUrl);
        WechatUserInfo wechatUserInfo = JSON.parseObject(userInfoJson, WechatUserInfo.class);
        return wechatUserInfo;
    }

    /**
     * 获取token信息
     *
     * @param code
     * @return
     */
    public AccessToken queryTokenInfo(String code) {
        String tokenUrl = String.format(genTokenUrl(code));
        String tokenJson = HttpUtil.httpGetJs(tokenUrl);
        AccessToken accessToken = JSON.parseObject(tokenJson, AccessToken.class);
        return accessToken;
    }

    /**
     * 获取企业Token信息
     *
     * @return
     */
    public CorpTokenInfo queryCorpToken() {
        String corpTokenUrl = genCorpTokenUrl(wechatInfoConfig.getCorpId(), wechatInfoConfig.getCorpSecret());
        String result = HttpUtil.httpGetJs(corpTokenUrl);
        CorpTokenInfo corpTokenInfo = JSON.parseObject(result, CorpTokenInfo.class);
        return corpTokenInfo;
    }

    /**
     * 主动推送消息
     *
     * @param accessToken
     * @param msgData
     * @return
     */
    public ActiveMsgResult activeSendMsg(String accessToken, String msgData) {
        String sendMsgUrl = genSendMsgUrl(accessToken);
        String result = HttpUtil.httpRequest(sendMsgUrl, HttpUtil.POST, msgData);
        ActiveMsgResult activeMsgResult = JSON.parseObject(result, ActiveMsgResult.class);
        return activeMsgResult;
    }

    /**
     * 创建菜单
     *
     * @param agentId
     * @param menuObj
     * @return
     */
    public CreateMenuResult createMenu(String agentId, String menuObj) {
        String menuUrl = genMenuUrl(agentId);
        String result = HttpUtil.httpRequest(menuUrl, HttpUtil.POST, menuObj);
        CreateMenuResult createMenuResult = JSON.parseObject(result, CreateMenuResult.class);
        return createMenuResult;
    }

    public String genMenuUrl(String agentId) {
        String menuUrl = wechatInfoConfig.getCreateMenu();
        menuUrl = menuUrl.replace("ACCESS_TOKEN", WeChatConstant.corpToken).replace("AGENTID", agentId);
        return menuUrl;
    }

    public String genCodeUrl(String redirectUrl, String state) {
        String codeUrl = wechatInfoConfig.getCodeUrl();
        codeUrl = codeUrl.replace("APPID", wechatInfoConfig.getAppId())
                .replace("REDIRECT_URI", URIUtil.encodeURIComponent(redirectUrl))
                .replace("SCOPE", wechatInfoConfig.getAuthScope())
                .replace("STATE", StringUtils.trimToEmpty(state));
        //log.info("genCodeUrl={}", codeUrl);
        return codeUrl;
    }

    public String genTokenUrl(String code) {
        String tokenUrl = wechatInfoConfig.getTokenUrl();
        tokenUrl = tokenUrl.replace("APPID", wechatInfoConfig.getAppId())
                .replace("SECRET", wechatInfoConfig.getAppSecret())
                .replace("CODE", code);
        //log.info("genTokenUrl={}", tokenUrl);
        return tokenUrl;
    }

    public String genUserInfoUrl(String token, String openid) {
        String userInfoUrl = wechatInfoConfig.getUserInfoUrl();
        userInfoUrl = userInfoUrl.replace("ACCESS_TOKEN", token)
                .replace("OPENID", openid);
        //log.info("genUserInfoUrl={}", userInfoUrl);
        return userInfoUrl;
    }

    public String genRefreshTokenUrl(String freshToken) {
        String refreshTokenUrl = wechatInfoConfig.getRefreshTokenUrl();
        refreshTokenUrl = refreshTokenUrl.replace("APPID", wechatInfoConfig.getAppId())
                .replace("REFRESH_TOKEN", freshToken);
        return refreshTokenUrl;
    }

    /**
     * 获取校验TOKEN URL
     *
     * @param accessToken
     * @param openid
     * @return
     */
    public String genValidTokenUrl(String accessToken, String openid) {
        String validTokenUrl = wechatInfoConfig.getValidTokenUrl();
        validTokenUrl = validTokenUrl.replace("ACCESS_TOKEN", accessToken)
                .replace("OPENID", openid);
        return validTokenUrl;
    }

    /**
     * 生成主动发送消息的URL
     *
     * @param accessToken
     * @return
     */
    public String genSendMsgUrl(String accessToken) {
        String sendMsgUrl = wechatInfoConfig.getSendMsgUrl();
        sendMsgUrl = sendMsgUrl.replace("ACCESS_TOKEN", accessToken);
        return sendMsgUrl;
    }

    public String genCorpTokenUrl(String corpId, String secret) {
        String corpTokenUrl = wechatInfoConfig.getCorpTokenUrl();
        corpTokenUrl = corpTokenUrl.replace("ID", corpId)
                .replace("SECRECT", secret);
        return corpTokenUrl;
    }

    /**
     * 上传临时素材URL
     *
     * @param accessToken
     * @param fileType
     * @return
     */
    public String genUploadUrl(String accessToken, String fileType) {
        String uploadUrl = wechatInfoConfig.getUploadUrl();
        uploadUrl = uploadUrl.replace("ACCESS_TOKEN", accessToken)
                .replace("TYPE", fileType);
        return uploadUrl;
    }

    /**
     * 下载临时素材URL
     *
     * @param accessToken
     * @param mediaId
     * @return
     */
    public String genDownLoadUrl(String accessToken, String mediaId) {
        String downLoadRrl = wechatInfoConfig.getDownLoadUrl();
        downLoadRrl = downLoadRrl.replace("ACCESS_TOKEN", accessToken)
                .replace("MEDIA_ID", mediaId);
        return downLoadRrl;
    }

    public static void main(String[] args) {
        String corpTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wwae679b71d1986931&corpsecret=UrBeKxk06y20zGq2b4NpuXfnQg_7wiOGAgnT0dp0wfU";
        corpTokenUrl = String.format(corpTokenUrl);
        String tokenInfo = HttpUtil.httpGetJs(corpTokenUrl);
        System.out.println("tokenInfo:" + tokenInfo);
    }
}
