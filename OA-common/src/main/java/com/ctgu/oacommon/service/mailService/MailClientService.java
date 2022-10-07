package com.ctgu.oacommon.service.mailService;

import com.ctgu.oacommon.dto.EmailAccountDTO;
import com.ctgu.oacommon.dto.EmailDTO;

import javax.mail.MessagingException;

/**
 * @author Zhang Jinming
 * @date 20/8/2022 下午2:10
 */
public interface MailClientService {
    void smtpMail(EmailAccountDTO emailAccountDTO, EmailDTO emailDTO) throws MessagingException;
}
