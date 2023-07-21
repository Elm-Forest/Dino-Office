package com.dino.common.service.mail;


import com.dino.common.bo.EmailBO;

import javax.mail.MessagingException;

/**
 * 邮件服务
 *
 * @author Zhang Jinming
 * @create 14/6/2022 下午5:16
 */
public interface MailService {
    /**
     * 发送自定义邮件
     *
     * @param emailDTO 收件对象
     * @throws MessagingException 消息异常
     */
    void sendMail(EmailBO emailDTO) throws MessagingException;
}
