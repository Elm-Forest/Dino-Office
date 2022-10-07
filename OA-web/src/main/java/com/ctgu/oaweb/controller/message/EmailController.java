package com.ctgu.oaweb.controller.message;

import com.ctgu.oacommon.entity.Email;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oacommon.vo.EmailAccountVO;
import com.ctgu.oacommon.vo.MailPageVO;
import com.ctgu.oacommon.vo.PageResult;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oamessage.service.core.EmailAccountService;
import com.ctgu.oamessage.service.core.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Elm Forest
 */
@RestController
@RequestMapping(value = "/message/mail")
public class EmailController {
    @Autowired
    private EmailAccountService emailAccountService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public void sendCode(EmailAccountVO emailAccountVO) {
        emailAccountService.sendCode(emailAccountVO);
    }

    @PostMapping("/account")
    public Result<?> registerEmailAccount(EmailAccountVO emailAccountVO) {
        return emailAccountService.registerEmailAccount(emailAccountVO);
    }

    @GetMapping("/account/check")
    public Result<?> checkExist() {
        return emailAccountService.checkExist();
    }

    @PostMapping
    public Result<?> sendMail(MailPageVO mailPageVO) {
        return emailService.sendMail(mailPageVO);
    }

    //获取所有自己发送的邮件(发件箱)
    @GetMapping("/aefOutBox")
    public Result<PageResult<Email>> getAllEmailFromOutBox(Integer current, Integer size) {
        Long id = UserThreadHolder.getCurrentUser().getId();
        return emailService.getAllEmailFromOutBox(id, current, size);
    }

    //获取所有自己接收的邮件(收件箱)
    @GetMapping("/aefInBox")
    public Result<PageResult<Email>> getAllEmailFromInBox(Integer current, Integer size) {
        Long id = UserThreadHolder.getCurrentUser().getId();
        return emailService.getAllEmailFromInBox(id, current, size);
    }

    //获取所有自己编写但是没有发送的邮件(草稿箱)
    @GetMapping("/aefDraftBox")
    public Result<PageResult<Email>> getAllEmailFromDraftBox(Integer current, Integer size) {
        Long id = UserThreadHolder.getCurrentUser().getId();
        return emailService.getAllEmailFromDraftBox(id, current, size);
    }

    //获取所有自己删除的的邮件(垃圾箱)
    @GetMapping("/aefGarbageBox")
    public Result<PageResult<Email>> getAllEmailFromGarbageBox(Integer current, Integer size) {
        Long id = UserThreadHolder.getCurrentUser().getId();
        return emailService.getAllEmailFromGarbageBox(id, current, size);
    }

    //删除对应id的邮件（这个只是将邮件放进垃圾箱里面，只有自己接收到的邮件删除后才会进入这个垃圾箱）
    @DeleteMapping("/detGarbageBox")
    public Result<Boolean> deleteEmailToGarbageBoxById(Long id) {
        return emailService.deleteEmailToGarbageBoxById(id);
    }

    //彻底删除对应id邮件（将垃圾箱里面的邮件彻底删除）
    @DeleteMapping("/daefGarbageBox")
    public Result<Boolean> deleteAleteEmailFromGarbageBoxById(Long id) {
        return emailService.deleteAleteEmailFromGarbageBoxById(id);
    }

    //删除自己发送的邮件还有自己写的草稿会执行这个方法（这个也是彻底删除）
    @DeleteMapping("/defBox")
    public Result<Boolean> deleteEmailFromBox(Long id) {
        return emailService.deleteEmailFromBox(id);
    }

    //从垃圾箱中恢复对应id的邮件
    @PostMapping("/refGarbageBox")
    public Result<Boolean> restoreEmailFromGarbageBox(Long id) {
        return emailService.restoreEmailFromGarbageBox(id);
    }
}
