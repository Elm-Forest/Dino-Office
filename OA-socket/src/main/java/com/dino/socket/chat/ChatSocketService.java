package com.dino.socket.chat;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.dino.common.models.dto.Result;
import com.dino.common.utils.ThreadHolder;
import com.dino.message.service.ChatService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Elm Forest
 */
@Component
public class ChatSocketService extends TextWebSocketHandler {
    private final static Logger logger = LogManager.getLogger(ChatSocketService.class);
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger();
    private WebSocketSession session;
    private static final ConcurrentHashMap<Long, WebSocketSession> SESSION_MAPS = new ConcurrentHashMap<>();
    private Long userId;

    @Resource
    private ChatService chatService;

    public void sendMessageToUserId(Long id, String message) throws IOException {
        Assert.notNull(id);
        Long uid = this.userId;
        chatService.saveMessage(id, uid, message);
        Result<?> messageBody = chatService.saveNotification(uid, id, message);
        if (SESSION_MAPS.containsKey(id)) {
            logger.info("{} send message to {}, message: {}", uid, id, message);
            WebSocketSession tarSession = SESSION_MAPS.get(id);
            tarSession.sendMessage(new TextMessage(JSONUtil.toJsonStr(messageBody)));
        }
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
        ChatSocketService.ONLINE_COUNT.incrementAndGet();
        logger.info("user {} build the connection, current online {}", userId, ONLINE_COUNT);
        ThreadHolder.remove();
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus) throws Exception {
        this.session.close();
        SESSION_MAPS.remove(this.userId);
        ChatSocketService.ONLINE_COUNT.decrementAndGet();
        logger.info("user {} close the connection, current online {}", userId, ONLINE_COUNT);
    }
}


