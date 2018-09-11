package com.wmall.wechat.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * socket核心配置容器
 */
@Configuration
@EnableWebSocketMessageBroker //注解表示开启使用STOMP协议来传输基于代理的消息，Broker就是代理的意思。
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override//方法用来配置消息代理，由于我们是实现推送功能，这里的消息代理是/topic
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");// /users 默认通知
        config.setApplicationDestinationPrefixes("/dcits"); // 设置服务器短的前缀
        //设置前端页面的前缀  默认是user 可以修改  点对点时使用
        config.setUserDestinationPrefix("/chat/");
    }

    @Override //方法表示注册STOMP协议的节点，并指定映射的URL
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //这一行代码用来注册STOMP协议节点，同时指定使用SockJS协议。
        registry.addEndpoint("/dcits-websocket").withSockJS();
    }


    @Bean
    public SocketSessionRegistry socketSessionRegistry() {
        return new SocketSessionRegistry();
    }

    @Bean
    public STOMPConnectEventListener stompConnectEventListener() {
        return new STOMPConnectEventListener();
    }
}