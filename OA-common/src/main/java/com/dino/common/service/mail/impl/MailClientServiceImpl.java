package com.dino.common.service.mail.impl;

import com.dino.common.bo.EmailAccountBO;
import com.dino.common.bo.EmailBO;
import com.dino.common.service.mail.MailClientService;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private Send send;

    @Override
    public void smtpMail(EmailAccountBO emailAccountBO, EmailBO emailBO) throws MessagingException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setPassword(emailAccountBO.getPassword());
        mailSender.setUsername(emailAccountBO.getEmail());
        mailSender.setHost(emailAccountBO.getHost());
        mailSender.setPort(emailAccountBO.getPort());
        mailSender.setProtocol(emailAccountBO.getProtocol());
        mailSender.setDefaultEncoding("utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.starttls.enable", "true");
        p.setProperty("mail.smtp.starttls.required", "true");
        mailSender.setJavaMailProperties(p);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(emailBO.getSubject());
        helper.setText(emailBO.getContent(), true);
        helper.setTo(emailBO.getEmail());
        helper.setFrom(emailAccountBO.getEmail());
        log.info(emailAccountBO.getEmail() + "正在发送邮件至" + emailBO.getEmail());
        send.sendSync(mailSender, mimeMessage);
    }
}
