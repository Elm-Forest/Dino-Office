package com.ctgu.web.controller;

import com.ctgu.common.utils.ThreadHolder;
import com.ctgu.web.websocket.WebSocketService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * WebSocketController
 *
 * @author Elm Forest
 */
@Api(tags = "WebSocketController")
@RestController
@RequestMapping("/socket")
public class WebSocketController {
    @Resource
    private WebSocketService webSocketService;

    @PostMapping("/msg")
    public void sentMessage(String message) {
        try {
            webSocketService.sendMessageToUserId(ThreadHolder.getCurrentUser().getId(), message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}