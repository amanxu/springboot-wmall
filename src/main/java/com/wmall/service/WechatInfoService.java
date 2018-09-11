package com.wmall.service;

import com.wmall.entity.WechatInfo;
import com.wmall.wechat.websocket.ChatUserInfo;

import java.util.List;

/**
 * @Description:
 * @Author: niexx <br>
 * @Date: 2017-11-24 9:43 <br>
 */
public interface WechatInfoService extends BaseService<WechatInfo> {

    public WechatInfo findChatById(String chatId);

    public List<WechatInfo> findChatsByCoryIdAndUserId( Integer agentId, String userId);

    public void updateChatInfoStatus(String ids) throws Exception;

    public void deleteChatInfo(String ids);

    public List<ChatUserInfo> queryUserList(Integer agentId);

}
