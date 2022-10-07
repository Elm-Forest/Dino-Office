package com.ctgu.oaweb.controller;

import com.ctgu.oacommon.dto.EmailDTO;
import com.ctgu.oacommon.service.mailService.MailService;
import com.ctgu.oacommon.utils.CommonUtils;
import com.ctgu.oacommon.vo.DateVO;
import com.ctgu.oacommon.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

/**
 * @author Zhang Jinming
 * @date 15/8/2022 下午4:06
 */
@RestController
public class TestController {
    @Autowired
    private MailService mailService;

    @RequestMapping("/test/email")
    public Result<?> email() throws MessagingException {
        String code = CommonUtils.getRandomCode();
        mailService.sendMail(EmailDTO.builder()
                .email("553781523@qq.com")
                .subject("【AutoOffice】开发者测试邮件")
                .content("这是一封测试邮件<br>" +
                        "您的验证码为" + code + "<br>15分钟有效哦~<br>" +
                        "如果您不清楚这封邮件的来源，非常抱歉我们误发了邮件<br>" +
                        "我们正在进行项目测试，如果您反复收到此邮件，烦请通过此邮箱与我们联系").build());
        return Result.ok();
    }

    @RequestMapping("/user/test")
    public Result<?> testUser() {
        return Result.ok("访问成功");
    }

    @RequestMapping("/test/time")
    public Result<?> testTime(DateVO date) {
        System.out.println(date);
        return Result.ok("访问成功");
    }
}
