package com.dino.web.controller.mail;

import com.dino.common.entity.Email;
import com.dino.common.models.dto.PageResult;
import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.BaseListVO;
import com.dino.common.models.vo.EmailAccountVO;
import com.dino.common.models.vo.MailPageVO;
import com.dino.mail.service.EmailAccountService;
import com.dino.mail.service.EmailService;
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

    @GetMapping
    public Result<?> selectOne(Long id) {
        return emailService.selectOne(id);
    }

    @GetMapping("/account/check")
    public Result<?> checkExist() {
        return emailAccountService.checkExist();
    }

    @PostMapping
    public Result<?> sendMail(MailPageVO mailPageVO) {
        return emailService.sendMail(mailPageVO);
    }

    @PostMapping("/draft")
    public Result<?> saveEmail(MailPageVO mailPageVO) {
        return emailService.saveEmail(mailPageVO);
    }

    @GetMapping("/contacts")
    public Result<?> selectContacts(String name) {
        return emailAccountService.selectContacts(name);
    }

    @ApiOperation("获取发件箱")
    @GetMapping("/outbox")
    public Result<?> selectOutBox(BaseListVO baseListVO) {
        return emailService.selectOutBox(baseListVO.getCurrent(), baseListVO.getSize());
    }

    @ApiOperation("获取收件箱")
    @GetMapping("/inbox")
    public Result<PageResult<Email>> selectInBox(BaseListVO baseListVO) {
        return emailService.selectInBox(baseListVO.getCurrent(), baseListVO.getSize());
    }

    @ApiOperation("获取草稿箱")
    @GetMapping("/draft")
    public Result<PageResult<Email>> selectDraftBox(BaseListVO baseListVO) {
        return emailService.selectDraftBox(baseListVO.getCurrent(), baseListVO.getSize());
    }

    @ApiOperation("获取垃圾箱")
    @GetMapping("/garbage")
    public Result<PageResult<Email>> selectGarbage(BaseListVO baseListVO) {
        return emailService.selectGarbage(baseListVO.getCurrent(), baseListVO.getSize());
    }

    @ApiOperation("移入垃圾箱")
    @DeleteMapping
    public Result<?> delete2Garbage(Long id) {
        return emailService.delete2Garbage(id);
    }

    @ApiOperation("彻底删除垃圾箱邮件")
    @DeleteMapping("/garbage")
    public Result<Boolean> removeFromGarbage(Long id) {
        return emailService.removeFromGarbage(id);
    }

    @ApiOperation("彻底删除草稿箱邮件")
    @DeleteMapping("/draft")
    public Result<Boolean> deleteFromDraft(Long id) {
        return emailService.deleteFromDraft(id);
    }

    @ApiOperation("从垃圾箱中恢复邮件")
    @PostMapping("/garbage/rec")
    public Result<Boolean> restoreFromGarbage(Long id) {
        return emailService.restoreFromGarbage(id);
    }
}
