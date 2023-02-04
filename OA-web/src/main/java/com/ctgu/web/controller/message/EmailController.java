package com.ctgu.web.controller.message;

import com.ctgu.common.entity.Email;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.EmailAccountVO;
import com.ctgu.common.models.vo.MailPageVO;
import com.ctgu.common.models.dto.PageResult;
import com.ctgu.common.utils.ThreadHolder;
import com.ctgu.message.service.core.EmailAccountService;
import com.ctgu.message.service.core.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Elm Forest
 */
@Api(tags = "邮件客户端管理")
@RestController
@RequestMapping(value = "/message/mail")
public class EmailController {
    @Resource
    private EmailAccountService emailAccountService;

    @Resource
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


    @ApiOperation("获取所有自己发送的邮件(发件箱)")
    @GetMapping("/outbox")
    public Result<PageResult<Email>> getAllEmailFromOutBox(Integer current, Integer size) {
        Long id = ThreadHolder.getCurrentUser().getId();
        return emailService.getAllEmailFromOutBox(id, current, size);
    }


    @ApiOperation("获取所有自己接收的邮件(收件箱)")
    @GetMapping("/inbox")
    public Result<PageResult<Email>> getAllEmailFromInBox(Integer current, Integer size) {
        Long id = ThreadHolder.getCurrentUser().getId();
        return emailService.getAllEmailFromInBox(id, current, size);
    }

    @ApiOperation("获取所有自己编写但是没有发送的邮件(草稿箱)")
    @GetMapping("/draft")
    public Result<PageResult<Email>> getAllEmailFromDraftBox(Integer current, Integer size) {
        Long id = ThreadHolder.getCurrentUser().getId();
        return emailService.getAllEmailFromDraftBox(id, current, size);
    }


    @ApiOperation("获取所有自己删除的的邮件(垃圾箱)")
    @GetMapping("/garbage")
    public Result<PageResult<Email>> getAllEmailFromGarbageBox(Integer current, Integer size) {
        Long id = ThreadHolder.getCurrentUser().getId();
        return emailService.getAllEmailFromGarbageBox(id, current, size);
    }

    @ApiOperation("删除对应id的邮件（这个只是将邮件放进垃圾箱里面，只有自己接收到的邮件删除后才会进入这个垃圾箱）")
    @DeleteMapping
    public Result<Boolean> deleteEmailToGarbageBoxById(Long id) {
        return emailService.deleteEmailToGarbageBoxById(id);
    }

    @ApiOperation("彻底删除对应id邮件（将垃圾箱里面的邮件彻底删除）")
    @DeleteMapping("/garbage")
    public Result<Boolean> deleteEmailFromGarbageBoxById(Long id) {
        return emailService.deleteEmailFromGarbageBoxById(id);
    }

    @ApiOperation("删除自己发送的邮件还有自己写的草稿会执行这个方法（这个也是彻底删除）")
    @DeleteMapping("/defBox")
    public Result<Boolean> deleteEmailFromBox(Long id) {
        return emailService.deleteEmailFromBox(id);
    }

    @ApiOperation("从垃圾箱中恢复对应id的邮件")
    @PostMapping("/garbage/rec")
    public Result<Boolean> restoreEmailFromGarbageBox(Long id) {
        return emailService.restoreEmailFromGarbageBox(id);
    }
}
