package com.dino.message.service;

import com.dino.common.models.dto.Result;

/**
 * @author Zhang Jinming
 */
public interface GPTService {
    /***
     * 保存请求消息
     * @param sessionId 会话id
     * @param content 内容
     */
    void saveRequestMessage(String sessionId, String content);

    /**
     * 保存响应消息
     *
     * @param sessionId 会话id
     * @param content   内容
     */
    void saveResponseMessage(String sessionId, String content);

    /**
     * 创建会话
     *
     * @param sessionId 会话id
     */
    void createSession(String sessionId);

    /**
     * 查询会话信息
     *
     * @param sessionId 会话id
     * @return 会话信息
     */
    Result<?> selectMessageInfo(String sessionId);

    /**
     * 查询会话列表
     *
     * @return 会话列表
     */
    Result<?> selectMessageLists();

    /**
     * 查询会话内容
     *
     * @param sessionId 会话id
     * @return 会话内容
     */
    String getSessionContent(String sessionId);

    /**
     * 查询会话标题
     *
     * @param sessionId 会话id
     */
    void setSessionContent(String sessionId, String content);

    /**
     * 查询会话标题
     *
     * @param sessionId 会话id
     */
    void setSessionTitle(String sessionId, String title);
}
