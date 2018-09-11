package com.wmall.controller;

import com.alibaba.fastjson.JSON;
import com.wmall.commons.MallResult;
import com.wmall.config.WechatInfoConfig;
import com.wmall.utils.ResultUtil;
import com.wmall.wechat.pojo.CreateMenuResult;
import com.wmall.wechat.service.WechatAccessService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: niexx <br>
 * @date: 2018-01-02 18:37 <br>
 */
@RestController
@RequestMapping("/wechatMenu")
public class CreateMenuController {

    private static Logger logger = LoggerFactory.getLogger(CreateMenuController.class);

    @Autowired
    private WechatInfoConfig wechatInfoConfig;

    @Autowired
    private WechatAccessService wechatAccessService;

    @RequestMapping("/create")
    public MallResult createMenu(@RequestParam(value = "corpId", required = false) String corpId,
                                 @RequestParam(value = "agentId", required = false) String agentId,
                                 @RequestParam(value = "appId", required = false) String appId,
                                 @RequestParam(value = "type") Integer type) {
        String interParam = null;
        if (type == null) {
            return ResultUtil.error("FFFF", "服务类型不能为空!!");
        }
        // 微信服务好
        if (type == 0) {
            if (StringUtils.isBlank(appId)) {
                return ResultUtil.error("FFFF", "appId不能为空!!");
            }
            interParam = "?appId=" + appId + "&type=" + type;
            // 微信企业号
        } else if (type == 1) {
            if (StringUtils.isBlank(corpId) || agentId == null) {
                return ResultUtil.error("FFFF", "corpId或者agentId参数不完整!!");
            }
            interParam = "?corpId=" + corpId + "&agentId=" + agentId + "&type=" + type;
        } else {
            return ResultUtil.error("FFFF", "暂不支持当前微信服务类型!!");
        }
        Map menuObj = genWeChatMenu(interParam);
        String menuStr = JSON.toJSONString(menuObj);
        CreateMenuResult createMenuResult = wechatAccessService.createMenu(agentId, menuStr);
        if (createMenuResult.getErrcode() == 0) {
            return ResultUtil.success();
        }
        return ResultUtil.error(createMenuResult.getErrcode() + "", createMenuResult.getErrrmsg());
    }

    public Map<String, Object> genWeChatMenu(String params) {
        Map<String, Object> wechatMenuMap = new HashMap<String, Object>(4);
        List<Map<String, Object>> wechatMenuMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> menuMapFirst = new HashMap<>();
        menuMapFirst.put("name", "创建工单");
        menuMapFirst.put("type", "view");
        menuMapFirst.put("key", "submitOrder");
        menuMapFirst.put("url", wechatInfoConfig.getRootUrl() + "/corpMenu/newIncident" + params);

        Map<String, Object> menuMapSec = new HashMap();
        menuMapSec.put("name", "工单记录");
        menuMapSec.put("type", "view");
        menuMapSec.put("key", "submitRecord");
        menuMapSec.put("url", wechatInfoConfig.getRootUrl() + "/corpMenu/myRequest" + params);

        Map<String, Object> menuMapThird = new HashMap();
        menuMapThird.put("name", "服务检索");
        menuMapThird.put("type", "view");
        menuMapThird.put("key", "searchThings");
        menuMapThird.put("url", wechatInfoConfig.getRootUrl() + "/corpMenu/searchThings" + params);

        wechatMenuMapList.add(menuMapFirst);
        wechatMenuMapList.add(menuMapSec);
        wechatMenuMapList.add(menuMapThird);
        wechatMenuMap.put("button", wechatMenuMapList);
        logger.info(wechatMenuMap.toString());
        return wechatMenuMap;
    }
}
