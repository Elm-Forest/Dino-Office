package com.dino.message.service;

import com.dino.common.models.dto.Result;

/**
 * @author Zhang Jinming
 */
public interface ChatService {

    /**
     * 保存私信
     *
     * @param auId    发送者id
     * @param suId    接收者id
     * @param content 内容
     */
    void saveMessage(Long auId, Long suId, String content);

    /**
     * 查询私信列表
     *
     * @param connectorId 私信联系人id
     * @return 私信列表
     */
    Result<?> selectMessage(Long connectorId);

    /**
     * 查询私信详情
     *
     * @param id 私信id
     * @return 私信详情
     */
    Result<?> selectConnectorInfo(Long id);

    /**
     * 查询私信联系人列表
     *
     * @return 私信联系人列表
     */
    Result<?> selectConnectorLists();


    /**
     * 保存通知
     *
     * @param suId    接收者id
     * @param auId    发送者id
     * @param message 内容
     */
    Result<?> saveNotification(Long suId, Long auId,String message);


    /**
     * 查询通知列表
     *
     * @return 通知列表
     */
    Result<?> selectNotification();

    /**
     * 删除通知
     *
     * @param id 通知id
     * @return 通知列表
     */
    Result<?> deleteNotification(String id);
}
