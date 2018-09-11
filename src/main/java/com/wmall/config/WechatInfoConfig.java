package com.wmall.config;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信参数配置
 *
 * @author niexx
 */
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatInfoConfig {

    private String appId;
    private String appSecret;
    /***企业ID*/
    private String corpId;
    /*** 应用的ID*/
    private String agentId;
    /***应用的凭证密钥*/
    private String corpSecret;
    /***项目根路径*/
    private String rootUrl;
    private String authScope;
    /***获取codeURL*/
    private String codeUrl;
    /*** 获取tokenURL*/
    private String tokenUrl;
    /***刷新tokenURL*/
    private String refreshTokenUrl;
    /***获取用户信息URL*/
    private String userInfoUrl;
    /*** 验证token的URL*/
    private String validTokenUrl;
    /***推送消息URL*/
    private String sendMsgUrl;
    /*** 获取企业TokenUrl*/
    private String corpTokenUrl;
    /***上传素材URL*/
    private String uploadUrl;
    /***下载素材URL*/
    private String downLoadUrl;
    /***文件保存根路径*/
    private String fileSavePath;
    /*** 过期时间*/
    private Long expireTime;
    /*** 创建菜单*/
    private String createMenu;
    /***删除菜单*/
    private String deleteMenu;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getAuthScope() {
        return authScope;
    }

    public void setAuthScope(String authScope) {
        this.authScope = authScope;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getRefreshTokenUrl() {
        return refreshTokenUrl;
    }

    public void setRefreshTokenUrl(String refreshTokenUrl) {
        this.refreshTokenUrl = refreshTokenUrl;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public void setUserInfoUrl(String userInfoUrl) {
        this.userInfoUrl = userInfoUrl;
    }

    public String getValidTokenUrl() {
        return validTokenUrl;
    }

    public void setValidTokenUrl(String validTokenUrl) {
        this.validTokenUrl = validTokenUrl;
    }

    public String getSendMsgUrl() {
        return sendMsgUrl;
    }

    public void setSendMsgUrl(String sendMsgUrl) {
        this.sendMsgUrl = sendMsgUrl;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getCorpSecret() {
        return corpSecret;
    }

    public void setCorpSecret(String corpSecret) {
        this.corpSecret = corpSecret;
    }

    public String getCorpTokenUrl() {
        return corpTokenUrl;
    }

    public void setCorpTokenUrl(String corpTokenUrl) {
        this.corpTokenUrl = corpTokenUrl;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getCreateMenu() {
        return createMenu;
    }

    public void setCreateMenu(String createMenu) {
        this.createMenu = createMenu;
    }

    public String getDeleteMenu() {
        return deleteMenu;
    }

    public void setDeleteMenu(String deleteMenu) {
        this.deleteMenu = deleteMenu;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
