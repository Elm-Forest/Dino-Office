package com.ctgu.message.service.core;

import com.ctgu.common.entity.Email;
import com.ctgu.common.models.vo.MailPageVO;
import com.ctgu.common.models.dto.PageResult;
import com.ctgu.common.models.dto.Result;

/**
 * @author Zhang Jinming
 * @date 20/8/2022 下午5:41
 */
public interface EmailService {
    /**
     * 发送邮件
     * @param mailPageVO 邮件VO对象
     * @return 是否发送成功
     */
    Result<?> sendMail(MailPageVO mailPageVO);

    /**
     *获取所有自己发送的邮件(发件箱)
     * @param id 当前用户id
     * @param current 页码
     * @param size 当页条数
     * @return 分页过后的满足条件的邮件集合
     */
    Result<PageResult<Email>> getAllEmailFromOutBox(Long id, Integer current, Integer size);

    /**
     *获取所有自己接收的邮件(收件箱)
     * @param id 当前用户id
     * @param current 页码
     * @param size 当页条数
     * @return 分页过后的满足条件的邮件集合
     */
    Result<PageResult<Email>> getAllEmailFromInBox(Long id, Integer current, Integer size);

    /**
     *获取所有自己编写但是没有发送的邮件(草稿箱)
     * @param id 当前用户id
     * @param current 页码
     * @param size 当页条数
     * @return 分页过后的满足条件的邮件集合
     */
    Result<PageResult<Email>> getAllEmailFromDraftBox(Long id, Integer current, Integer size);

    /**
     *获取所有自己删除的的邮件(垃圾箱)
     * @param id 当前用户id
     * @param current 页码
     * @param size 当页条数
     * @return 分页过后的满足条件的邮件集合
     */
    Result<PageResult<Email>> getAllEmailFromGarbageBox(Long id, Integer current, Integer size);

    /**
     * 删除对应id的邮件（这个只是将邮件放进垃圾箱里面，只有自己接收到的邮件删除后才会进入这个垃圾箱）
     * @param id 邮件id
     * @return 是否操作成功
     */
    Result<Boolean> deleteEmailToGarbageBoxById(Long id);

    /**
     * 彻底删除对应id邮件（将垃圾箱里面的邮件彻底删除）
     * @param id 邮件id
     * @return 是否操作成功
     */
    Result<Boolean> deleteEmailFromGarbageBoxById(Long id);

    /**
     * 删除自己发送的邮件还有自己写的草稿会执行这个方法（这个也是彻底删除）
     * @param id 邮件id
     * @return 是否操作成功
     */
    Result<Boolean> deleteEmailFromBox(Long id);

    /**
     * 从垃圾箱中恢复对应id的邮件
     * @param id 邮件id
     * @return 是否操作成功
     */
    Result<Boolean> restoreEmailFromGarbageBox(Long id);

    //Result<Boolean> insertEmailToDraftBox();
}
