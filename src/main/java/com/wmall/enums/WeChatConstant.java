package com.wmall.enums;

/**
 * @Description: 全局变量类
 * @Author: niexx <br>
 * @Date: 2017-11-23 18:43 <br>
 */
public class WeChatConstant {

    /**** 企业token*/
    public static String corpAccessToken = null;
    public static String corpAccessTicket = null;

    /*** 推送消息URL*/
    public static String sendMsgUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";
    /***获取企业TokenUrl*/
    public static String corpTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ID&corpsecret=SECRECT";
    /*** 上传素材URL*/
    public static String uploadUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    /*** 下载素材URL*/
    public static String downLoadUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
    /*** 部门成员信息的URL*/
    public static String corpUserInfoUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID";
    /*** 获取codeURL*/
    public static String codeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    /*** 获取tokenURL*/
    public static String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    /*** 刷新tokenURL*/
    public static String refreshTokenUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
    /***获取用户信息URL*/
    public static String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    /*** 验证token的URL*/
    public static String validTokenUrl = "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";
}
