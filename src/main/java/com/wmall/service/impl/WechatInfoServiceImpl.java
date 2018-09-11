package com.wmall.service.impl;

import com.google.common.collect.Lists;
import com.wmall.entity.WechatInfo;
import com.wmall.service.WechatInfoService;
import com.wmall.wechat.enums.ChatInfoReadStatueEnum;
import com.wmall.wechat.websocket.ChatUserInfo;
import com.wmall.wechat.websocket.SocketSessionRegistry;
import com.wmall.wechat.websocket.WebOutputMsgDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @Description:
 * @Author: niexx <br>
 * @Date: 2017-11-24 10:08 <br>
 */
@Service
public class WechatInfoServiceImpl extends BaseServiceImpl<WechatInfo> implements WechatInfoService {

    @Autowired
    private SocketSessionRegistry socketSessionRegistry;

    @Override
    public WechatInfo findChatById(String chatId) {
        return super.fetch("chatId", chatId);
    }

    @Override
    public List<WechatInfo> findChatsByCoryIdAndUserId(Integer agentId, String userId) {
        Assert.notNull(agentId, "应用AgentId不能为空!!");
        Assert.notNull(userId, "用户UserId不能为空!!");
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("from WechatInfo where agentId = ? and userId = ? order by createTime desc");
        List<Object> paramList = Lists.newArrayList();
        paramList.add(agentId);
        paramList.add(userId);
        List<WechatInfo> wechatInfoList = findByHql(hqlBuffer.toString(), paramList);
        return wechatInfoList;
    }

    @Override
    public void updateChatInfoStatus(String ids) throws Exception {
        Assert.notNull(ids, "更新消息状态，IDS不能为空!!");
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            WechatInfo wechatInfo = findChatById(id);
            if (wechatInfo == null) throw new Exception("被更新消息记录不存在");
            wechatInfo.setReadState(ChatInfoReadStatueEnum.READ.getCode());
            saveOrUpdate(wechatInfo);
        }
    }

    @Override
    public void deleteChatInfo(String ids) {
        Assert.notNull(ids, "更新消息状态，IDS不能为空!!");
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            super.deleteByKey("chatId", id);
        }
    }

    @Override
    public List<ChatUserInfo> queryUserList(Integer agentId) {
        // 根据UserId分组插叙
        StringBuffer hqlBuf = new StringBuffer();
        hqlBuf.append("from WechatInfo info where 1=1");
        hqlBuf.append(" and info.agentId = ?");
        hqlBuf.append(" group by info.userId");

        List<Object> paramList = Lists.newArrayList(agentId);
        List<WechatInfo> chatInfoList = findByHql(hqlBuf.toString(), paramList);
        // 返回用户列表信息，同时返回未读消息
        List<ChatUserInfo> userInfoList = Lists.newArrayList();
        if (chatInfoList != null) {
            for (WechatInfo wechatInfo : chatInfoList) {
                ChatUserInfo chatUserInfo = new ChatUserInfo();
                chatUserInfo.setUserId(wechatInfo.getUserId());
                chatUserInfo.setAgentId(wechatInfo.getAgentId());
                // 查询当前用户未读的消息
                List<WechatInfo> unreadMsgList = queryUnreadMsg(wechatInfo.getUserId(), wechatInfo.getAgentId());
                if (unreadMsgList != null && unreadMsgList.size() > 0) {
                    List<WebOutputMsgDto> outputMsgDtoList = Lists.newArrayList();
                    for (WechatInfo chatInfo : unreadMsgList) {
                        WebOutputMsgDto outputMsgDto = new WebOutputMsgDto();
                        BeanUtils.copyProperties(chatInfo, outputMsgDto);// 属性复制
                        outputMsgDtoList.add(outputMsgDto);
                    }
                    chatUserInfo.setUnreadCount(outputMsgDtoList.size());
                    chatUserInfo.setOutputMsgDtoList(outputMsgDtoList);
                }
                userInfoList.add(chatUserInfo);
            }
        }
        return userInfoList;
    }

    /**
     * 查询当前应用内当前用户的未读消息
     *
     * @param userId
     * @param agentId
     * @return
     */
    private List<WechatInfo> queryUnreadMsg(String userId, Integer agentId) {
        StringBuffer hqlBuf = new StringBuffer();
        hqlBuf.append("from WechatInfo where userId = ? and agentId = ? and readState = 1 order by createTime desc");
        List<Object> paramList = Lists.newArrayList();
        paramList.add(userId);
        paramList.add(agentId);
        List<WechatInfo> unreadMsgList = findByHql(hqlBuf.toString(), paramList);
        return unreadMsgList;
    }
}
