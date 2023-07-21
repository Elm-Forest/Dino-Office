package com.dino.web.controller.socket;

import com.dino.socket.chat.ChatSocketService;
import com.dino.socket.gpt.GPTSocketService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * WebSocketController
 *
 * @author Elm Forest
 */
@Api(tags = "SocketController")
@RestController
@RequestMapping("/socket")
public class SocketController {
    @Resource
    private ChatSocketService chatSocketService;

    @Resource
    private GPTSocketService gptSocketService;

    @PostMapping("/chat")
    public void chatWithTeammates(Long id, String message) {
        try {
            chatSocketService.sendMessageToUserId(id, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/gpt")
    public void chatGpt(String sessionId, String message) {
        try {
            gptSocketService.communicating(sessionId, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/gpt/start")
    public String createSession() {
        return gptSocketService.createSession();
    }

    @PostMapping("/gpt/recover")
    public void recoverSession(String sessionId) {
        gptSocketService.recoverSession(sessionId);
    }

    @DeleteMapping("/gpt/remove")
    public void removeSession(String sessionId) {
        gptSocketService.removeSession(sessionId);
    }
}