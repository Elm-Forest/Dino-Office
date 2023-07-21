package com.dino.web.controller.message;

import com.dino.common.models.dto.Result;
import com.dino.message.service.GPTService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Zhang Jinming
 */
@Api(tags = "GPT助手模块")
@RestController
@RequestMapping(value = "/message/gpt")
public class GptController {
    @Resource
    private GPTService gptService;

    @GetMapping("/info")
    public Result<?> getGptMessageInfo(String sessionId) {
        return gptService.selectMessageInfo(sessionId);
    }

    @GetMapping("/lists")
    public Result<?> getGptMessageLists() {
        return gptService.selectMessageLists();
    }
}
