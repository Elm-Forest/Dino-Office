package com.dino.common.service.mail.impl;

import com.dino.common.bo.EmailBO;
import com.dino.common.service.mail.MailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * @author Zhang Jinming
 * @create 14/6/2022 下午5:17
 */
@Log4j2
@Service
public class MailServiceImpl implements MailService {

    @Resource
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String hostMail;

    @Resource
    private Send send;

    @Override
    public void sendMail(EmailBO emailBO) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(emailBO.getSubject());
        helper.setText(emailBO.getContent(), true);
        helper.setTo(emailBO.getEmail());
        helper.setFrom(hostMail);
        send.sendAsync(mailSender, mimeMessage);
        log.info("正在发送邮件至" + emailBO.getEmail());
    }
}
