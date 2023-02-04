package com.ctgu.web.handler;

import com.ctgu.web.websocket.WebSocketService;
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
    private WebSocketService webSocketService;

    @Override
    public void onApplicationEvent(@NotNull ContextClosedEvent event) {
        try {
            webSocketService.closeAllSessions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Websocket closed");
    }
}