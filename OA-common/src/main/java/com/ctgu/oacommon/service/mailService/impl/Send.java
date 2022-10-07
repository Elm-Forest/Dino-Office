package com.ctgu.oacommon.service.mailService.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * @author Zhang Jinming
 * @create 14/6/2022 下午6:19
 */
@Service
public class Send {
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Async
    public void send(MimeMessage mimeMessage) {
        this.mailSender.send(mimeMessage);
        System.out.println("已发送");
    }

    @Async
    public void send(JavaMailSenderImpl mailSender, MimeMessage mimeMessage) {
        mailSender.send(mimeMessage);
        System.out.println("已发送！");
    }
}
