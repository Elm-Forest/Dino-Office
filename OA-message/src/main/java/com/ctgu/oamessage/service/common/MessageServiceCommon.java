package com.ctgu.oamessage.service.common;

import com.ctgu.oacommon.entity.Message;
import com.ctgu.oacommon.vo.Result;

import java.util.List;

/**
 * @author 李嘉超
 * @version 1.0
 * @date 2022/8/16 19:21
 */
public interface MessageServiceCommon {
    /**
     * 这个是用来查看用户所有已经收到的消息，其中没有考虑消息过期，事实上用户应该只能看到消息没有过期的
     * 根据接收用户ID的所有已发布的消息
     * @param id 接收用户ID
     * @return 消息集合
     */
    List<Message> getAllHaveReleasedMessageByAcceptUserId(Long id);

    /**
     * 用户根据接收者来查看自己未发送的消息
     * 根据接收用户ID查询所有的未发送消息
     * @param id 接收用户ID
     * @return 消息集合
     */
    List<Message> getAllNotReleaseMessageByAcceptUserId(Long id);

}
