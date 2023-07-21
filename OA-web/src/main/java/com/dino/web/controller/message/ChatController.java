package com.dino.web.controller.message;

import com.dino.common.models.dto.Result;
import com.dino.message.service.ChatService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Zhang Jinming
 */
@Api(tags = "私信模块")
@RestController
@RequestMapping(value = "/message/chat")
public class ChatController {
    @Resource
    private ChatService chatService;

    @GetMapping("/lists")
    public Result<?> getChatList(Long connectorId) {
        return chatService.selectMessage(connectorId);
    }

    @GetMapping("/connector/lists")
    public Result<?> getConnectorLists() {
        return chatService.selectConnectorLists();
    }

    @GetMapping("/connector/info")
    public Result<?> getConnectorInfo(Long id) {
        return chatService.selectConnectorInfo(id);
    }

    @GetMapping("/notification")
    public Result<?> getNotification() {
        return chatService.selectNotification();
    }

    @DeleteMapping("/notification/delete")
    public Result<?> deleteNotification(String id) {
        return chatService.deleteNotification(id);
    }
}
