package com.wmall.service;

/**
 * @description:
 * @author: niexx <br>
 * @date: 2018-04-28 18:56 <br>
 */
public interface WeChatTokenService {

    /**
     * 查询或刷新access token
     */
    public String refreshCorpToken();

    /**
     * 查询或刷新JS Ticket
     */
    public String refreshCorpTicket();
}
