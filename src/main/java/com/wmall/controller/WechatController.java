package com.wmall.controller;

import com.wmall.commons.MallResult;
import com.wmall.entity.WechatInfo;
import com.wmall.service.WechatInfoService;
import com.wmall.utils.ResultUtil;
import com.wmall.wechat.pojo.AccessToken;
import com.wmall.wechat.pojo.WechatUserInfo;
import com.wmall.wechat.service.WechatAccessService;
import com.wmall.wechat.service.WechatMsgService;
import com.wmall.wechat.websocket.ChatUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * slyritd.mynatapp.cc
 * 测试账号信息：
 * appID：wx7220dc9281a9518e
 * appsecret：d4624c36b6795d1d99dcf0547af5443d
 */
//@RestController
@RestController
@RequestMapping("/wechat")
public class WechatController extends BaseController {

    Logger logger = LoggerFactory.getLogger(WechatController.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Value("${wechat.rootUrl}")
    private String serverRootUrl;

    @Autowired
    private WechatAccessService wechatAccessService;

    @Autowired
    private WechatMsgService chatMsgService;

    @Autowired
    private WechatInfoService wechatInfoService;

    @RequestMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String state) {
        // 跳转到本服务的URL
        String redirectUrl = serverRootUrl + "/wechat/userInfo";
        // 获取code的微信URL
        String codeUrl = wechatAccessService.genCodeUrl(redirectUrl, state);
        //log.info("微信授权，获取带code的跳转URL={}", codeUrl);
        return "redirect:" + codeUrl;
    }

    @RequestMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code, @RequestParam("state") String state) {
        AccessToken accessToken = wechatAccessService.queryTokenInfo(code);
        logger.info("微信授权，获取tokenObj={}", accessToken);
        WechatUserInfo wechatUserInfo = wechatAccessService.queryUserInfo(accessToken.getOpenid(), accessToken.getAccess_token());
        logger.info("微信OpenId和token获取用户信息userInfo={}", wechatUserInfo);
        return "redirect:" + state;
    }

    /**
     * 配置微信接收消息API接口
     *
     * @param request  msg_signature 企业微信加密签名，msg_signature结合了企业填写的token、请求中的timestamp、nonce参数、加密的消息体
     *                 timestamp 时间戳  nonce 随机数
     *                 echostr 	加密的随机字符串，以msg_encrypt格式提供。需要解密并返回echostr明文，解密后有random、msg_len、msg、$CorpID四个字段，
     *                 其中msg即为echostr明文
     * @param response
     * @return
     */
    @GetMapping("/chatService")
    public void wechatApiAuth(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam("msg_signature") String msgSign, @RequestParam("timestamp") String timeStamp,
                              @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) {
        logger.info("msgSign={},timeStamp={},nonce={},echostr={}", msgSign, timeStamp, nonce, echostr);
        try {
            String msg = chatMsgService.verifyURL(msgSign, timeStamp, nonce, echostr);
            response.getWriter().print(msg);
            logger.info("微信URL验证，返回的明文信息：{}", msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理企业微信的消息服务
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/chatService")
    public void wechatService(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam("msg_signature") String msgSign, @RequestParam("timestamp") String timeStamp,
                              @RequestParam("nonce") String nonce) {
        String reqJs = readJs(request);
        String resultMsg = chatMsgService.receiveMsg(msgSign, timeStamp, nonce, reqJs);
        logger.info("接收消息接口，通知前端的消息，msgBody:{}；outputMsg:{}", reqJs, resultMsg);
        // 返回给微信用户消息
        //wechatAccessService.activeSendMsg(WeChatConstant.corpToken, resultMsg);
    }


    @RequestMapping("/queryChats")
    public MallResult sendMsg(@RequestParam("agentId") Integer agentId,
                              @RequestParam("userId") String userId) {
        List<WechatInfo> wechatInfoList = wechatInfoService.findChatsByCoryIdAndUserId(agentId, userId);
        return ResultUtil.success(wechatInfoList);
    }

    /**
     * 根据ID更新消息状态
     *
     * @param ids
     * @return
     */
    @RequestMapping("/updateMsg")
    public MallResult updateMsgStatus(@RequestParam("ids") String ids) {
        if (StringUtils.isEmpty(ids)) {
            return ResultUtil.error("FFFFFF", "ids参数不能为空!");
        }
        try {
            wechatInfoService.updateChatInfoStatus(ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }

    /**
     * 查询用户列表，返回用户列表以及用户下未读的信息
     *
     * @param agentId
     * @return
     */
    @RequestMapping("/queryUsers")
    public MallResult queryUsers(@RequestParam("agentId") Integer agentId) {
        List<ChatUserInfo> userInfoList = wechatInfoService.queryUserList(agentId);
        return ResultUtil.success(userInfoList);
    }

    /**
     * 根据ID删除对应的记录
     *
     * @param ids
     * @return
     */
    @RequestMapping("/deleteChats")
    public MallResult deleteChatRecord(@RequestParam("ids") String ids) {
        wechatInfoService.deleteChatInfo(ids);
        return ResultUtil.success();
    }
}
