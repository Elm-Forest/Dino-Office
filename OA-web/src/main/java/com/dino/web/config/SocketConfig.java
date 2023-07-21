package com.dino.web.config;

import com.dino.socket.chat.ChatSocketService;
import com.dino.socket.gpt.GPTSocketService;
import com.dino.web.handler.WebSocketInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;


/**
 * @author Elm Forest
 */
@Configuration
public class SocketConfig implements WebSocketConfigurer {
    @Resource
    private WebSocketInterceptor webSocketInterceptor;

    @Resource
    private ChatSocketService chatSocketService;

    @Resource
    private GPTSocketService gptSocketService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatSocketService,
                        "/connection/chat", "/connection/wait")
                .addHandler(gptSocketService,
                        "/connection/gpt")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
    }
}