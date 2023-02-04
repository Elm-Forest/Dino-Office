package com.ctgu.web.config;

import com.ctgu.web.handler.WebSocketInterceptor;
import com.ctgu.web.websocket.WebSocketService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;


/**
 * @author Elm Forest
 */
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    @Resource
    private WebSocketInterceptor webSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketService(), "/websocket")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketService webSocketService() {
        return new WebSocketService();
    }
}