package com.ctgu.oamessage.service.core;

import com.ctgu.oacommon.entity.Message;
import com.ctgu.oacommon.vo.MessageConditionVO;
import com.ctgu.oacommon.vo.MessageVO;
import com.ctgu.oacommon.vo.PageResult;
import com.ctgu.oacommon.vo.Result;


/**
 * @author 李嘉超
 * @version 1.0
 * @date 2022/8/16 16:22
 */
public interface MessageServiceCore {
    /**
     * 下面的都是查询的方法
     */
    /**
     * 根据用户ID查看用户所有未发布的消息
     * 返回对应用户ID的未发布的消息
     * @param id 发送用户ID
     * @param current 当前页码
     * @param size 当页条数
     * @return 所有该ID用户未发布的消息
     */
    Result<PageResult<Message>> getAllNotReleaseMessageBySendUserId(Long id,Integer current,Integer size);
    /**
     * 根据用户ID查看用户所有已发布的消息
     * 返回对应用户ID的已发布的消息
     * @param id 发送用户ID
     *@param current 当前页码
     *@param size 当页条数
     * @return 所有该ID用户以发布的消息
     */
    Result<PageResult<Message>> getAllHaveReleasedMessageBySendUserId(Long id,Integer current,Integer size);

    /**
     * 这个查询的时候还有一个过期限制，就是说用户应该只能看见的是没有过期的消息，
     * 而过期的消息应该对用户不可见，这个限制只对已发送的消息有效，而对未发送的消息没有过期限制
     * 查询用户可以看到的所有的已经送达到用户手中的消息
     * @param id 用户ID
     * @param current 当前页码
     * @param size 当页条数
     * @return 消息集合
     */
    Result<PageResult<Message>> getAllNotOverdueAndHaveReleasedMessageByAcceptUserId(Long id,Integer current,Integer size);

    /**
     * 下面都是插入的方法
     */
    /**
     * 保存消息到数据库，这是不发布消息，仅仅是保存保存，以后可以修改
     * @param messageVO 前端传来的MessageVO对象
     * @return 是否保存成功
     */
    Result<Boolean> insertMessageNotRelease(MessageVO messageVO);
    /**
     * 保存消息到数据库，这是发布消息，发送消息，以后不可以修改
     * @param messageVO 前端传来的MessageVO对象
     * @return 是否保存成功
     */
    Result<Boolean> insertMessageHaveRelease(MessageVO messageVO);


    /**
     * 更新方法
     */
    /**
     * 更改未发送的消息
     * @param messageVO 前端传来的更改后的MessageVO对象
     * @return 是否更新成功
     */
    Result<Boolean> updateMessageNotRelease(MessageVO messageVO);


    /**
     * 删除方法
     */
    /**
     * 删除对应ID的消息
     * @param id 要删除的消息id
     * @return 是否保删除成功
     */
    Result<Boolean> deleteMessageById(Long id);

    /**
     * 下面是条件查询方法
     */
    /**
     * 用户根据条件来查询所有自己已发送的消息
     * @param messageConditionVO 条件查询对象
     * @return 满足条件查询的消息集合
     */
    Result<PageResult<Message>> getAllHaveReleasedMessageByCondition(Long currentUserId,MessageConditionVO messageConditionVO);

    /**
     * 用户根据条件来查询所有自己未发送的消息
     * @param messageConditionVO 条件查询的对象
     * @return 满足条件查询的消息集合
     */
    Result<PageResult<Message>> getAllNotReleasedMessageByCondition(Long currentUserId,MessageConditionVO messageConditionVO);

    /**
     * 用户获取根据条件获取所有自己收到的并且可以看到的消息（消息过期）
     * @param messageConditionVO 条件查询对象
     * @return 满足条件查询的消息集合
     */
    Result<PageResult<Message>> getAllNotOverdueAndHaveReleasedMessage(Long currentUserId,MessageConditionVO messageConditionVO);
}
