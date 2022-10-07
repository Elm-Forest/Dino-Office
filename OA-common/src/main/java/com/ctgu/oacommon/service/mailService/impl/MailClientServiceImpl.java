package com.ctgu.oacommon.service.mailService.impl;

import com.ctgu.oacommon.dto.EmailAccountDTO;
import com.ctgu.oacommon.dto.EmailDTO;
import com.ctgu.oacommon.service.mailService.MailClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author Zhang Jinming
 * @date 20/8/2022 下午2:10
 */
@Service
public class MailClientServiceImpl implements MailClientService {
    @Autowired
    private Send send;

    @Override
    public void smtpMail(EmailAccountDTO emailAccountDTO, EmailDTO emailDTO) throws MessagingException {
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
        send.send(mailSender, mimeMessage);
        System.out.println("正在发送邮件至" + emailDTO.getEmail());
    }

}
