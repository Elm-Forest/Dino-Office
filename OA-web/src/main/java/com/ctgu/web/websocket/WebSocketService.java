package com.ctgu.web.websocket;

import cn.hutool.core.lang.Assert;
import com.ctgu.common.utils.ThreadHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Elm Forest
 */
public class WebSocketService extends TextWebSocketHandler {
    private final static Logger logger = LogManager.getLogger(WebSocketService.class);
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger();
    private WebSocketSession session;
    private static final ConcurrentHashMap<Long, WebSocketSession> SESSION_MAPS = new ConcurrentHashMap<>();
    private Long userId;

    public void sendMessageToUserId(Long id, String message) throws IOException {
        logger.info("{}发送消息到{},消息：{}", userId, id, message);
        Assert.notNull(id);
        WebSocketSession tarSession = SESSION_MAPS.get(id);
        tarSession.sendMessage(new TextMessage(message));
    }

    public void closeAllSessions() throws IOException {
        for (WebSocketSession item : SESSION_MAPS.values()) {
            Assert.notNull(item);
            item.close();
        }
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        this.session = session;
        this.userId = ThreadHolder.getCurrentUser().getId();
        SESSION_MAPS.put(this.userId, this.session);
        WebSocketService.ONLINE_COUNT.incrementAndGet();
        logger.info("用户{}连接成功,当前在线人数为{}", userId, ONLINE_COUNT);
        ThreadHolder.remove();
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus) throws Exception {
        this.session.close();
        SESSION_MAPS.remove(this.userId);
        WebSocketService.ONLINE_COUNT.decrementAndGet();
        logger.info("用户{}关闭连接！当前在线人数为{}", userId, ONLINE_COUNT);
    }
}


