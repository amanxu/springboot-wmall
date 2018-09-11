package com.wmall.controller;

        import com.wmall.wechat.service.WechatAccessService;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description:
 * @author: niexx <br>
 * @date: 2018-01-02 22:49 <br>
 */
@Controller
@RequestMapping("/corpMenu")
public class CorpMenuController {

    private static Logger logger = LoggerFactory.getLogger(CorpMenuController.class);

    @Autowired
    private WechatAccessService wechatAccessService;

    @RequestMapping("/newIncident")
    public String newIncident(@RequestParam(value = "corpId", required = false) String corpId,
                              @RequestParam(value = "agentId", required = false) String agentId,
                              @RequestParam(value = "appId", required = false) String appId,
                              @RequestParam("type") Integer type, Model model) {
        logger.info("type={};corpId={};agentId={},appId={}", type, corpId, agentId, appId);
        // 微信服务号
        if (type == 0) {
            model.addAttribute("appId", appId);
            // 微信企业号
        } else if (type == 1) {
            model.addAttribute("corpId", corpId);
            model.addAttribute("agentId", agentId);
            return "/menuViewCorp";
        }
        return null;
    }
}
