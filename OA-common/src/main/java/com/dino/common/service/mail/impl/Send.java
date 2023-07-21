package com.dino.common.service.mail.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 * @author Zhang Jinming
 * @create 14/6/2022 下午6:19
 */
@Log4j2
@Component
public class Send {
    @Async
    public void sendAsync(JavaMailSenderImpl mailSender, MimeMessage mimeMessage) {
        mailSender.send(mimeMessage);
        log.info("已发送！");
    }

    public void sendSync(JavaMailSenderImpl mailSender, MimeMessage mimeMessage) {
        mailSender.send(mimeMessage);
        log.info("已发送！");
    }
}
