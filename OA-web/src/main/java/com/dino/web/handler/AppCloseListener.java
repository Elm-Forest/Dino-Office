package com.dino.web.handler;


import com.dino.socket.chat.ChatSocketService;
import com.dino.socket.gpt.GPTSocketService;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Elm Forest
 * Springboot主进程停止时执行
 */
@Log4j2
@Component
public class AppCloseListener implements ApplicationListener<ContextClosedEvent> {

    @Resource
    private ChatSocketService chatSocketService;
    @Resource
    private GPTSocketService gptSocketService;

    @Override
    public void onApplicationEvent(@NotNull ContextClosedEvent event) {
        try {
            chatSocketService.closeAllSessions();
            gptSocketService.closeAllSessions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Websocket closed");
    }
}