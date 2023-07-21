package com.dino.socket.gpt.config;

import cn.hutool.json.JSONUtil;
import com.dino.common.constants.OpenAiConst;
import com.dino.common.entity.User;
import com.dino.common.utils.ThreadHolder;
import com.dino.message.service.GPTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @author Elm Forest
 */
@Slf4j
@Component
public class OpenAIWebSocketEventSourceListener extends EventSourceListener {

    private WebSocketSession session;

    private final StringBuilder stringBuilder = new StringBuilder();

    private String sessionId;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @Resource
    private GPTService gptService;

    public OpenAIWebSocketEventSourceListener() {
    }

    public OpenAIWebSocketEventSourceListener(WebSocketSession session, String sessionId) {
        this.session = session;
        this.sessionId = sessionId;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOpen(@NotNull EventSource eventSource,
                       @NotNull Response response) {
        log.info("OpenAI建立sse连接...");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String data) {
        log.info("OpenAI返回数据：{}", data);
        if (data.equals(OpenAiConst.END_FLAG)) {
            log.info("OpenAI返回数据结束了");
            ThreadHolder.addCurrentUserTmp(user);
            gptService.saveResponseMessage(sessionId, getCompleteString());
            ThreadHolder.removeTmp();
            Map<String, String> map = new HashMap<>();
            map.put("content", OpenAiConst.END_FLAG);
            session.sendMessage(new TextMessage(JSONUtil.toJsonStr(map)));
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        ChatCompletionResponse completionResponse = mapper.readValue(data, ChatCompletionResponse.class);
        Message temp = completionResponse.getChoices().get(0).getDelta();
        if (temp.getContent() != null && !Objects.equals(temp.getContent(), "")) {
            stringBuilder.append(temp.getContent());
        }
        String delta = mapper.writeValueAsString(temp);
        session.sendMessage(new TextMessage(delta));
    }


    public String getCompleteString() {
        String completeString = stringBuilder.toString();
        stringBuilder.setLength(0);
        return completeString;
    }

    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        log.info("OpenAI关闭连接...");
    }


    @SneakyThrows
    @Override
    public void onFailure(@NotNull EventSource eventSource, Throwable t, Response response) {
        if (Objects.isNull(response)) {
            return;
        }
        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("OpenAI连接异常data：{}，异常：{}", body.string(), t);
        } else {
            log.error("OpenAI连接异常data：{}，异常：{}", response, t);
        }
        eventSource.cancel();
    }
}