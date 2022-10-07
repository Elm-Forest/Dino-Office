package com.ctgu.oacommon.service.mailService;


import com.ctgu.oacommon.dto.EmailDTO;

import javax.mail.MessagingException;

/**
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
    void sendMail(EmailDTO emailDTO) throws MessagingException;
}
