package com.wmall.service.impl;

import com.wmall.config.WechatInfoConfig;
import com.wmall.enums.WeChatConstant;
import com.wmall.queue.DelayedQueueService;
import com.wmall.service.WeChatTokenService;
import com.wmall.wechat.pojo.CorpTokenInfo;
import com.wmall.wechat.service.WechatAccessService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: niexx <br>
 * @date: 2018-04-28 19:01 <br>
 */
@Service
public class WeChatTokenServiceImpl implements WeChatTokenService {

    private static Logger logger = LoggerFactory.getLogger(WeChatTokenServiceImpl.class);

    @Autowired
    private WechatAccessService wechatAccessService;

    @Autowired
    private DelayedQueueService<String, String> delayedQueueService;

    @Autowired
    private WechatInfoConfig wechatInfoConfig;

    @Override
    public String refreshCorpToken() {
        CorpTokenInfo corpTokenInfo = wechatAccessService.queryCorpToken();
        if (corpTokenInfo != null) {
            if (StringUtils.isBlank(corpTokenInfo.getAccess_token())) {
                logger.info("获取TOKEN失败：{}", corpTokenInfo.toString());
                return null;
            }
            delayedQueueService.put(wechatInfoConfig.getCorpId(), corpTokenInfo.getAccess_token(), wechatInfoConfig.getExpireTime());
            WeChatConstant.corpAccessToken = corpTokenInfo.getAccess_token();
            return corpTokenInfo.getAccess_token();
        }
        return null;
    }

    @Override
    public String refreshCorpTicket() {
        return null;
    }
}
