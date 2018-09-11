package com.wmall.scheduled;

import com.wmall.enums.WeChatConstant;
import com.wmall.utils.JsonUtil;
import com.wmall.wechat.pojo.CorpTokenInfo;
import com.wmall.wechat.service.WechatAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description: 定时更新Token，Token有效期7200s
 * @author: niexx <br>
 * @Date: 2017-11-23 18:05 <br>
 * 此类暂时先不使用，企业微信客服和企业微信业务的token公用，暂时采用EnterpriseAccessTokenThread.java获取
 */
@Component
public class CorpTokenScheduled {
    private static Logger logger = LoggerFactory.getLogger(CorpTokenScheduled.class);

    @Autowired
    private WechatAccessService wechatAccessService;

    @PostConstruct
    @Scheduled(cron = "0 */30 * * * ?")
    public void getAccessToken() throws Exception {
        CorpTokenInfo corpTokenInfo = wechatAccessService.queryCorpToken();
        if (corpTokenInfo == null) {
            throw new Exception("获取企业TOKEN失败!!!");
        }
        WeChatConstant.corpAccessToken = corpTokenInfo.getAccess_token();
        logger.info("获取企业AccessToken:{}", JsonUtil.toJson(corpTokenInfo));
    }
}
