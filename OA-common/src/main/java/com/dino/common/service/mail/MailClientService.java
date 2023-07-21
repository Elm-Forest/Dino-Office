package com.dino.common.service.mail;

import com.dino.common.bo.EmailAccountBO;
import com.dino.common.bo.EmailBO;

import javax.mail.MessagingException;

/**
 * 邮件客户端服务
 *
 * @author Zhang Jinming
 * @date 20/8/2022 下午2:10
 */
public interface MailClientService {
    /**
     * 使用smtp发送邮件
     *
     * @param emailAccountDTO 邮件账户信息
     * @param emailDTO        邮件信息
     * @throws MessagingException 邮件发送异常
     */
    void smtpMail(EmailAccountBO emailAccountDTO, EmailBO emailDTO) throws MessagingException;
}
