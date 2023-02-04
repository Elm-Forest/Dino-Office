package com.ctgu.common.service.mail.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;

import javax.mail.internet.MimeMessage;

/**
 * @author Zhang Jinming
 * @create 14/6/2022 下午6:19
 */
@Log4j2
public class Send {
    @Async
    public void send(JavaMailSenderImpl mailSender, MimeMessage mimeMessage) {
        mailSender.send(mimeMessage);
        log.info("已发送！");
    }
}
