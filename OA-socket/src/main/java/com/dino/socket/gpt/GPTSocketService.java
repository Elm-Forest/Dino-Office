package com.dino.socket.gpt;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.dino.common.utils.ThreadHolder;
import com.dino.message.service.GPTService;
import com.dino.socket.gpt.config.LocalCache;
import com.dino.socket.gpt.config.OpenAIWebSocketEventSourceListener;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.dino.common.constants.OpenAiConst.TITLE_PROMPT;


/**
 * @author Elm Forest
 */
@Component
public class GPTSocketService extends TextWebSocketHandler {
    private final static Logger logger = LogManager.getLogger(GPTSocketService.class);
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger();
    private WebSocketSession session;
    private static final ConcurrentHashMap<String, WebSocketSession> SESSION_MAPS = new ConcurrentHashMap<>();
    private String sessionId;
    @Resource
    private OpenAiStreamClient openAiStreamClient;

    @Resource
    private OpenAiClient openAiClientV2;

    @Resource
    private GPTService gptService;

    @Resource
    private OpenAIWebSocketEventSourceListener eventSourceListener;

    public void communicating(String sessionId, String message) throws IOException {
        logger.info("{} send message to gpt, message: {}", sessionId, message);
        WebSocketSession tarSession = SESSION_MAPS.get(sessionId);
        eventSourceListener.setSession(tarSession);
        eventSourceListener.setSessionId(sessionId);
        eventSourceListener.setUser(ThreadHolder.getCurrentUser());
        String messageContext = (String) LocalCache.CACHE.get(sessionId);
        gptService.saveRequestMessage(sessionId, message);
        List<Message> messages = new ArrayList<>();
        if (StrUtil.isNotBlank(messageContext)) {
            messages = JSONUtil.toList(messageContext, Message.class);
            if (messages.size() >= 10) {
                messages = messages.subList(1, 10);
            }
            Message currentMessage = Message.builder().content(message).role(Message.Role.ASSISTANT).build();
            messages.add(currentMessage);
        } else {
            Message currentMessage = Message.builder().content(message).role(Message.Role.ASSISTANT).build();
            messages.add(currentMessage);
            createTitle(sessionId, message);
        }
        tarSession.sendMessage(new TextMessage("[START_CONTENT]"));
        openAiStreamClient.streamChatCompletion(messages, eventSourceListener);
        String jsonStr = JSONUtil.toJsonStr(messages);
        gptService.setSessionContent(sessionId, jsonStr);
        LocalCache.CACHE.put(sessionId, jsonStr, LocalCache.TIMEOUT);
    }

    public void createTitle(String sessionId, String content) throws IOException {
        WebSocketSession tarSession = SESSION_MAPS.get(sessionId);
        tarSession.sendMessage(new TextMessage("[START_TITLE]"));
        CompletionResponse completions = openAiClientV2.completions(TITLE_PROMPT + content);
        String title = completions.getChoices()[0].getText();
        tarSession.sendMessage(new TextMessage(title));
        tarSession.sendMessage(new TextMessage("[DONE]"));
        gptService.setSessionTitle(sessionId, title);
    }

    public String createSession() {
        String sessionId = IdUtil.randomUUID();
        this.sessionId = sessionId;
        SESSION_MAPS.put(this.sessionId, this.session);
        gptService.createSession(sessionId);
        GPTSocketService.ONLINE_COUNT.incrementAndGet();
        logger.info("user {} build the connection, current online {}", this.sessionId, ONLINE_COUNT);
        return sessionId;
    }

    public void recoverSession(String sessionId) {
        this.sessionId = sessionId;
        SESSION_MAPS.put(this.sessionId, this.session);
        String sessionContent = gptService.getSessionContent(sessionId);
        LocalCache.CACHE.put(sessionId, sessionContent, LocalCache.TIMEOUT);
        GPTSocketService.ONLINE_COUNT.incrementAndGet();
        logger.info("user {} build the connection, current online {}", this.sessionId, ONLINE_COUNT);
    }

    public void removeSession(String sessionId) {
        SESSION_MAPS.remove(sessionId);
        GPTSocketService.ONLINE_COUNT.decrementAndGet();
        logger.info("user {} close the connection, current online {}", sessionId, ONLINE_COUNT);
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
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus) throws Exception {
        this.session.close();
        SESSION_MAPS.remove(this.sessionId);
        GPTSocketService.ONLINE_COUNT.decrementAndGet();
        logger.info("user {} close the connection, current online {}", this.sessionId, ONLINE_COUNT);
        ThreadHolder.remove();
    }
}


