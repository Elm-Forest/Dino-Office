package com.ctgu.common.service.mail.impl;

import com.ctgu.common.bo.EmailAccountBO;
import com.ctgu.common.bo.EmailBO;
import com.ctgu.common.service.mail.MailClientService;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author Zhang Jinming
 * @date 20/8/2022 下午2:10
 */
@Log4j2
@Service
public class MailClientServiceImpl implements MailClientService {

    @Override
    public void smtpMail(EmailAccountBO emailAccountDTO, EmailBO emailDTO) throws MessagingException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setPassword(emailAccountDTO.getPassword());
        mailSender.setUsername(emailAccountDTO.getEmail());
        mailSender.setHost(emailAccountDTO.getHost());
        mailSender.setPort(emailAccountDTO.getPort());
        mailSender.setProtocol(emailAccountDTO.getProtocol());
        mailSender.setDefaultEncoding("utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.starttls.enable", "true");
        p.setProperty("mail.smtp.starttls.required", "true");
        mailSender.setJavaMailProperties(p);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(emailDTO.getSubject());
        helper.setText(emailDTO.getContent(), true);
        helper.setTo(emailDTO.getEmail());
        helper.setFrom(emailAccountDTO.getEmail());
        new Send().send(mailSender, mimeMessage);
        log.info(emailAccountDTO.getEmail() + "正在发送邮件至" + emailDTO.getEmail());
    }
}
