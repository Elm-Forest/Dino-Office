package com.ctgu.common.service.mail;

import com.ctgu.common.bo.EmailAccountBO;
import com.ctgu.common.bo.EmailBO;

import javax.mail.MessagingException;

/**
 * @author Zhang Jinming
 * @date 20/8/2022 下午2:10
 */
public interface MailClientService {
    void smtpMail(EmailAccountBO emailAccountDTO, EmailBO emailDTO) throws MessagingException;
}
