package com.ctgu.message.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.common.bo.EmailAccountBO;
import com.ctgu.common.bo.EmailBO;
import com.ctgu.common.constants.EmailConst;
import com.ctgu.common.dao.message.EmailAccountMapper;
import com.ctgu.common.dao.message.EmailMapper;
import com.ctgu.common.dao.user.UserInfoMapper;
import com.ctgu.common.entity.Email;
import com.ctgu.common.entity.EmailAccount;
import com.ctgu.common.exception.BizException;
import com.ctgu.common.models.dto.PageResult;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.MailPageVO;
import com.ctgu.common.service.mail.MailClientService;
import com.ctgu.common.utils.Assert;
import com.ctgu.common.utils.ThreadHolder;
import com.ctgu.message.service.EmailService;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;

import static com.ctgu.common.constants.EmailConst.DRAFT_BOX;
import static com.ctgu.common.constants.EmailConst.MAIL_BOX;
import static com.ctgu.common.utils.AesUtils.decryptAes;

/**
 * @author Zhang Jinming
 * @date 20/8/2022 下午5:41
 */
@Log4j2
@Service
public class EmailServiceImpl implements EmailService {
    @Resource
    private MailClientService mailClientService;

    @Resource
    private EmailAccountMapper emailAccountMapper;

    @Resource
    private EmailMapper emailMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> sendMail(MailPageVO mailPageVO) {
        Long id = ThreadHolder.getCurrentUser().getId();
        EmailAccount emailAccount = emailAccountMapper.selectById(id);
        Assert.objNotNull(emailAccount, new BizException("您尚未绑定邮箱"));
        EmailAccount targetEmailAccount = emailAccountMapper.selectById(mailPageVO.getAuId());
        Assert.objNotNull(targetEmailAccount, new BizException("对方尚未绑定邮箱"));
        int insert = emailMapper.insert(Email.builder()
                .suId(id)
                .auId(mailPageVO.getAuId())
                .emailTitle(mailPageVO.getSubject())
                .emailContent(mailPageVO.getContent())
                .acceptEmailType(MAIL_BOX)
                .sendEmailType(MAIL_BOX)
                .attachmentPath(null)
                .sendTime(DateUtil.date())
                .build());
        Assert.greaterThanZero(insert, new RuntimeException("发件失败"));
        try {
            mailClientService.smtpMail(EmailAccountBO.builder()
                    .email(emailAccount.getEmail())
                    .password(decryptAes(emailAccount.getPassword()))
                    .host(emailAccount.getHost())
                    .port(emailAccount.getPort())
                    .protocol(emailAccount.getProtocol())
                    .build(), EmailBO.builder()
                    .email(targetEmailAccount.getEmail())
                    .subject(mailPageVO.getSubject())
                    .content(mailPageVO.getContent())
                    .build());
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return Result.ok();
    }

    @Override
    public Result<?> saveEmail(MailPageVO mailPageVO) {
        Long id = ThreadHolder.getCurrentUser().getId();
        EmailAccount emailAccount = emailAccountMapper.selectById(id);
        Assert.objNotNull(emailAccount, new BizException("您尚未绑定邮箱"));
        EmailAccount targetEmailAccount = emailAccountMapper.selectById(mailPageVO.getAuId());
        Assert.objNotNull(targetEmailAccount, new BizException("对方尚未绑定邮箱"));
        if (mailPageVO.getId() != null) {
            Email email = emailMapper.selectById(mailPageVO.getId());
            if (email != null) {
                email.setAuId(mailPageVO.getAuId());
                email.setEmailTitle(mailPageVO.getSubject());
                email.setEmailContent(mailPageVO.getContent());
                int update = emailMapper.updateById(email);
                Assert.greaterThanZero(update, new RuntimeException("保存失败"));
            }
        } else {
            int insert = emailMapper.insert(Email.builder()
                    .suId(id)
                    .auId(mailPageVO.getAuId())
                    .emailTitle(mailPageVO.getSubject())
                    .emailContent(mailPageVO.getContent())
                    .acceptEmailType(DRAFT_BOX)
                    .sendEmailType(DRAFT_BOX)
                    .attachmentPath(null)
                    .sendTime(DateUtil.date())
                    .build());
            Assert.greaterThanZero(insert, new RuntimeException("保存失败"));
        }
        return Result.ok();
    }

    @Override
    public Result<?> selectOne(Long id) {
        Email email = emailMapper.selectById(id);
        email.setAcceptUserName(userInfoMapper.selectById(email.getAuId()).getName());
        return Result.ok(email);
    }

    /**
     * 获取发件箱
     */
    @Override
    public Result<?> selectOutBox(Integer current, Integer size) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Page<Email> page = new Page<>(current, size);
        page = emailMapper.selectPage(page, new LambdaQueryWrapper<Email>()
                .eq(Email::getSuId, id)
                .orderByDesc(Email::getSendTime)
                .in(Email::getSendEmailType,
                        EmailConst.MAIL_BOX,
                        EmailConst.RESTORE_BOX));
        return Result.ok(pageResultAddName(page));
    }

    /**
     * 获取收件箱
     */
    @Override
    public Result<PageResult<Email>> selectInBox(Integer current, Integer size) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Page<Email> page = new Page<>(current, size);
        emailMapper.selectPage(page, new LambdaQueryWrapper<Email>()
                .eq(Email::getAuId, id)
                .orderByDesc(Email::getSendTime)
                .in(Email::getAcceptEmailType,
                        EmailConst.MAIL_BOX,
                        EmailConst.RESTORE_BOX));
        return Result.ok(pageResultAddName(page));
    }

    /**
     * 获取草稿箱
     */
    @Override
    public Result<PageResult<Email>> selectDraftBox(Integer current, Integer size) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Page<Email> page = new Page<>(current, size);
        emailMapper.selectPage(page, new LambdaQueryWrapper<Email>()
                .eq(Email::getSuId, id)
                .orderByDesc(Email::getSendTime)
                .in(Email::getSendEmailType,
                        DRAFT_BOX));
        return Result.ok(pageResultAddName(page));
    }

    /**
     * 获取所有自己删除的的邮件(垃圾箱)
     * 这个里面只保存别人发给自己，自己又删除的邮件，
     * 而对于自己发送出去的消息或者删除自己编写到草稿箱里面的邮件删除后都不会进入回收站
     */
    @Override
    public Result<PageResult<Email>> selectGarbage(Integer current, Integer size) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Page<Email> page = new Page<>(current, size);
        emailMapper.selectPage(page, new LambdaQueryWrapper<Email>()
                .eq(Email::getAuId, id)
                .and(i -> i.in(Email::getAcceptEmailType, EmailConst.BIN_BOX))
                .or()
                .eq(Email::getSuId, id)
                .and(i -> i.in(Email::getSendEmailType, EmailConst.BIN_BOX))
                .orderByDesc(Email::getSendTime));
        return Result.ok(pageResultAddName(page));
    }

    /**
     * 删除对应id的邮件（删除收件箱中的邮件时调用的方法,实际是更新操作）
     */
    @Override
    public Result<Boolean> delete2Garbage(Long id) {
        Email email = emailMapper.selectById(id);
        Long userId = ThreadHolder.getCurrentUser().getId();
        if (email.getAuId().equals(userId)) {
            email.setAcceptEmailType(EmailConst.BIN_BOX);
        } else {
            email.setSendEmailType(EmailConst.BIN_BOX);
        }
        int update = emailMapper.updateById(email);
        Assert.greaterThanZero(update, new RuntimeException("删除失败"));
        return Result.ok();
    }

    /**
     * 彻底删除对应id邮件（将垃圾箱里面的邮件彻底删除）
     */
    @Override
    public Result<Boolean> removeFromGarbage(Long id) {
        Email email = emailMapper.selectById(id);
        Long userId = ThreadHolder.getCurrentUser().getId();
        if (email.getAuId().equals(userId)) {
            email.setAcceptEmailType(EmailConst.ALETE_BOX);
        } else {
            email.setSendEmailType(EmailConst.ALETE_BOX);
        }
        int update = emailMapper.updateById(email);
        Assert.greaterThanZero(update, new RuntimeException("删除失败"));
        return Result.ok();
    }

    /**
     * 删除自己发送的邮件还有自己写的草稿会执行这个方法这个也是彻底删除
     */
    @Override
    public Result<Boolean> deleteFromDraft(Long id) {
        Email email = emailMapper.selectById(id);
        email.setSendEmailType(EmailConst.ALETE_BOX);
        int update = emailMapper.updateById(email);
        Assert.greaterThanZero(update, new RuntimeException("删除失败"));
        return Result.ok();
    }

    /**
     * 从垃圾箱中恢复对应id的邮件
     */
    @Override
    public Result<Boolean> restoreFromGarbage(Long id) {
        Email email = emailMapper.selectById(id);
        Long userId = ThreadHolder.getCurrentUser().getId();
        if (email.getAuId().equals(userId)) {
            email.setAcceptEmailType(EmailConst.RESTORE_BOX);
        } else {
            email.setSendEmailType(EmailConst.RESTORE_BOX);
        }
        int update = emailMapper.updateById(email);
        Assert.greaterThanZero(update, new RuntimeException("恢复失败"));
        return Result.ok();
    }

    @NotNull
    private PageResult<Email> pageResultAddName(Page<Email> page) {
        List<Email> records = page.getRecords();
        PageResult<Email> pageResult = new PageResult<>(page.getRecords(), (int) page.getTotal());
        if (!ObjectUtils.isEmpty(records)) {
            for (Email email : records) {
                String sendName = userInfoMapper.selectById(email.getSuId()).getName();
                String acceptName = userInfoMapper.selectById(email.getAuId()).getName();
                email.setAcceptUserName(acceptName);
                email.setSendName(sendName);
            }
            pageResult = new PageResult<>(page.getRecords(), (int) page.getTotal());
        }
        return pageResult;
    }
}
