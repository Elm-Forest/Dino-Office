package com.ctgu.oacommon.service.mailService.impl;

import com.ctgu.oacommon.dto.EmailDTO;
import com.ctgu.oacommon.service.mailService.MailService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Zhang Jinming
 * @create 14/6/2022 下午5:17
 */
@Data
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private Send send;

    @Value("${spring.mail.username}")
    private String hostMail;


    @Override
    public void sendMail(EmailDTO emailDTO) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(emailDTO.getSubject());
        helper.setText(emailDTO.getContent(), true);
        helper.setTo(emailDTO.getEmail());
        helper.setFrom(hostMail);
        send.send(mimeMessage);
        System.out.println("正在发送邮件至" + emailDTO.getEmail());
    }
}
