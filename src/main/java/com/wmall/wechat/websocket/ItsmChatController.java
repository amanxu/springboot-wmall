package com.wmall.wechat.websocket;

import com.wmall.config.WechatInfoConfig;
import com.wmall.controller.BaseController;
import com.wmall.entity.WechatInfo;
import com.wmall.service.WechatInfoService;
import com.wmall.wechat.enums.ChatInfoOwnEnum;
import com.wmall.wechat.enums.ChatInfoReadStatueEnum;
import com.wmall.wechat.enums.ChatMsgTypeEnum;
import com.wmall.wechat.enums.WeChatConstant;
import com.wmall.wechat.pojo.ActiveMsgResult;
import com.wmall.wechat.service.ChatMsgAssemble;
import com.wmall.wechat.service.WechatAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 聊天控制器
 */
@Controller
@RequestMapping("/chatService")
public class ItsmChatController extends BaseController {

    Logger logger = LoggerFactory.getLogger(ItsmChatController.class);
    @Autowired
    private ChatMsgAssemble chatMsgAssemble;

    @Autowired
    private WechatInfoConfig wechatInfoConfig;

    @Autowired
    private WechatAccessService wechatAccessService;

    @Autowired
    private WechatInfoService wechatInfoService;

    //session操作类
    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;
    //消息发送工具
    @Autowired
    private SimpMessagingTemplate template;

    @RequestMapping(value = "/index")
    public String index() {
        return "/index";
    }

    @RequestMapping(value = "/chatOne")
    public String chatOne() {
        return "/chatOne";
    }

    @RequestMapping(value = "/chatTwo")
    public String chatTwo() {
        return "/chatTwo";
    }

    @RequestMapping(value = "/chatMsg")
    public String chatMessage() {
        return "/chatMessage";
    }

    /**
     * 用户广播
     * 发送消息广播 目标客户为：所有开启websocket的客户端
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/broadcast")
    @ResponseBody
    public WebOutputMsgDto sendToCommUserMessage(HttpServletRequest request) {
        List<String> keys = webAgentSessionRegistry.getAllSessionIds().entrySet()
                .stream().map(Map.Entry::getKey)
                .collect(Collectors.toList());
        Date date = new Date();
        keys.forEach(x -> {
            String sessionId = webAgentSessionRegistry.getSessionIds(x).stream().findFirst().get().toString();
            template.convertAndSendToUser(sessionId, "/topic/greetings", new WebOutputMsgDto("Commmsg：allsend, " + "Send " + date.getTime()), createHeaders(sessionId));
        });
        return new WebOutputMsgDto("Send For All Customer, " + new Date());
    }


    /**
     * 同样的发送消息   只不过是ws版本  http请求不能访问
     * 根据用户ID发送消息
     *
     * @param inputMsgDto
     * @return
     * @throws Exception
     */
    @MessageMapping("/customer")
    public void chatCustomer(WebInputMsgDto inputMsgDto) throws Exception {
        String agentId = inputMsgDto.getAgentId().toString();
        //这里没做校验
        String sessionId = webAgentSessionRegistry.getSessionIds(agentId).stream().findFirst().get();
        WebOutputMsgDto outputMsgDto = new WebOutputMsgDto();
        outputMsgDto.setAgentId(inputMsgDto.getAgentId());
        outputMsgDto.setUserId(inputMsgDto.getUserId());
        outputMsgDto.setChatContent("from " + inputMsgDto.getUserId() + ":" + inputMsgDto.getChatContent());
        template.convertAndSendToUser(sessionId, "/topic/greetings", outputMsgDto, createHeaders(sessionId));
    }

    /**
     * @param inputMsgDto
     * @throws Exception
     */
    @MessageMapping("/itsmChatCustomer") // websocket请求路径中不用加项目根路径wmall
    public void itsmChatCustomer(WebInputMsgDto inputMsgDto) throws Exception {
        logger.info("UserId={},AgentId={}", inputMsgDto.getUserId(), inputMsgDto.getAgentId());
        //TODO(文本和图片 如何区分处理？？)
        //1.持久化客服消息到数据库(文本或者图片消息)
        persistKfMsg(inputMsgDto);
        //2.将客服消息发送至微信用户
        String sendMsg = chatMsgAssemble.assembleSendTextMsg(inputMsgDto.getUserId(),
                inputMsgDto.getAgentId(), inputMsgDto.getChatContent());
        ActiveMsgResult activeMsgResult = wechatAccessService.activeSendMsg(WeChatConstant.corpToken, sendMsg);
        logger.info("客服消息回复结果：{}", activeMsgResult);
    }

    /**
     * 持久化客服消息到数据库
     *
     * @param inputMsgDto
     */
    private void persistKfMsg(WebInputMsgDto inputMsgDto) {
        WechatInfo wechatInfo = new WechatInfo();
        wechatInfo.setCorpId(wechatInfoConfig.getCorpId());
        wechatInfo.setMsgOwn(ChatInfoOwnEnum.CORP.getCode());
        wechatInfo.setMsgType(ChatMsgTypeEnum.TEXT.getCode());
        wechatInfo.setReadState(ChatInfoReadStatueEnum.READ.getCode());
        wechatInfo.setUserId(inputMsgDto.getUserId());
        wechatInfo.setAgentId(inputMsgDto.getAgentId());
        wechatInfo.setChatContent(inputMsgDto.getChatContent());
        wechatInfo.setCreateTime(new Date());
        String chatId = chatMsgAssemble.genChatId("TXT");
        wechatInfo.setChatId(chatId);
        wechatInfoService.saveOrUpdate(wechatInfo);
    }

}
