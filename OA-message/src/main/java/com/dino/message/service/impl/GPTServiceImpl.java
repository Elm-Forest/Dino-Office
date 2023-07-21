package com.dino.message.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dino.common.dao.message.ChatMapper;
import com.dino.common.dao.message.GPTMapper;
import com.dino.common.dao.message.GptChatMapper;
import com.dino.common.entity.Chat;
import com.dino.common.entity.GPT;
import com.dino.common.entity.GptChat;
import com.dino.common.models.dto.Result;
import com.dino.common.utils.Assert;
import com.dino.common.utils.ThreadHolder;
import com.dino.message.service.GPTService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.dino.common.constants.OpenAiConst.GPT_ID;

/**
 * @author Zhang Jinming
 */
@Service
public class GPTServiceImpl implements GPTService {
    @Resource
    private ChatMapper chatMapper;

    @Resource
    private GPTMapper gptMapper;

    @Resource
    private GptChatMapper gptChatMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRequestMessage(String sessionId, String content) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Chat chat = Chat.builder()
                .auId(GPT_ID)
                .suId(id)
                .content(content)
                .time(DateUtil.date())
                .build();
        int flag = chatMapper.insert(chat);
        Assert.greaterThanZero(flag, new RuntimeException("保存失败"));
        flag = gptChatMapper.insert(GptChat.builder()
                .chatId(chat.getId())
                .sessionId(sessionId)
                .build());
        Assert.greaterThanZero(flag, new RuntimeException("保存失败"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveResponseMessage(String sessionId, String content) {
        Long id = ThreadHolder.getCurrentUserTmp().getId();
        Chat chat = Chat.builder()
                .auId(id)
                .suId(GPT_ID)
                .content(content)
                .time(DateUtil.date())
                .build();
        int flag = chatMapper.insert(chat);
        Assert.greaterThanZero(flag, new RuntimeException("保存失败"));
        flag = gptChatMapper.insert(GptChat.builder()
                .chatId(chat.getId())
                .sessionId(sessionId)
                .build());
        Assert.greaterThanZero(flag, new RuntimeException("保存失败"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSession(String sessionId) {
        int flag = gptMapper.insert(GPT.builder()
                .userId(ThreadHolder.getCurrentUser().getId())
                .time(DateUtil.date())
                .sessionId(sessionId)
                .build());
        Assert.greaterThanZero(flag, new RuntimeException("保存失败"));
    }

    @Override
    public Result<?> selectMessageInfo(String sessionId) {
        return Result.ok(gptChatMapper.selectGptChats(sessionId));
    }

    @Override
    public Result<?> selectMessageLists() {
        return Result.ok(gptMapper.selectList(new LambdaQueryWrapper<GPT>()
                .eq(GPT::getUserId, ThreadHolder.getCurrentUser().getId()).orderByDesc(GPT::getTime)));
    }

    @Override
    public String getSessionContent(String sessionId) {
        return gptMapper.selectOne(new LambdaQueryWrapper<GPT>()
                        .eq(GPT::getSessionId, sessionId))
                .getSessionContent();
    }

    @Override
    public void setSessionContent(String sessionId, String content) {
        int flag = gptMapper.update(GPT.builder()
                .sessionContent(content)
                .time(DateUtil.date())
                .build(), new LambdaQueryWrapper<GPT>()
                .eq(GPT::getSessionId, sessionId));
        Assert.greaterThanZero(flag, new RuntimeException("保存失败"));
    }

    @Override
    public void setSessionTitle(String sessionId, String title) {
        int flag = gptMapper.update(GPT.builder()
                .title(title)
                .build(), new LambdaQueryWrapper<GPT>()
                .eq(GPT::getSessionId, sessionId));
        Assert.greaterThanZero(flag, new RuntimeException("保存失败"));
    }
}
