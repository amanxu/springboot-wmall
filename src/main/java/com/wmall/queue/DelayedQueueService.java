package com.wmall.queue;

import com.wmall.service.WeChatTokenService;
import com.wmall.wechat.service.WechatAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

/**
 * 延时队列缓存短信
 *
 * @param <K>
 * @param <V>
 * @author niexx <br>
 * 2017年5月18日 下午5:20:23 <br>
 * @version 0.1 <br>
 */
@Component
public class DelayedQueueService<K, V> {

    private static Logger logger = LoggerFactory.getLogger(DelayedQueueService.class);

    @Autowired
    private WeChatTokenService weChatTokenService;

    public ConcurrentHashMap<K, V> map = new ConcurrentHashMap<K, V>();
    public DelayQueue<DelayedQueueItem<K>> queue = new DelayQueue<DelayedQueueItem<K>>();

    public void put(K k, V v, long liveTime) {
        V v2 = map.put(k, v);
        DelayedQueueItem<K> tempItem = new DelayedQueueItem<K>(k, liveTime);
        if (v2 != null) {
            queue.remove(tempItem);
        }
        queue.put(tempItem);
    }

    public DelayedQueueService() {
        Thread t = new Thread() {
            @Override
            public void run() {
                checkExpiredKey();
            }
        };
        t.setDaemon(true);
        t.start();
    }

    public void checkExpiredKey() {
        while (true) {
            DelayedQueueItem<K> delayedItem = queue.poll();
            if (delayedItem != null) {
                // 从队列中取出，同时调用接口获取token更新redis或者其他缓存机制中的参数
                weChatTokenService.refreshCorpToken();
            }
            try {
                Thread.sleep(1000 * 10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 参数缓存测试类
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        int liveTime = 3;
        DelayedQueueService<String, Map<String, String>> delayedQueueService = new DelayedQueueService<String, Map<String, String>>();
        Map<String, String> weChatMap = new HashMap<>();
        weChatMap.put("appId.token", "my_token");
        weChatMap.put("appId.ticket", "my_ticket");
        delayedQueueService.put("appId", weChatMap, liveTime);

        Thread.sleep(5000);
        System.out.println("==sleep weakup==");
    }

}
