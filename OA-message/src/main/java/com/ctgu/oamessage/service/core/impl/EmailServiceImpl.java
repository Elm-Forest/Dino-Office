package com.ctgu.oamessage.service.core.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.oacommon.constant.EmailConst;
import com.ctgu.oacommon.dto.EmailAccountDTO;
import com.ctgu.oacommon.dto.EmailDTO;
import com.ctgu.oacommon.entity.Email;
import com.ctgu.oacommon.entity.EmailAccount;
import com.ctgu.oacommon.exception.BizException;
import com.ctgu.oacommon.service.mailService.MailClientService;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oacommon.vo.MailPageVO;
import com.ctgu.oacommon.vo.PageResult;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oamessage.mapper.EmailAccountMapper;
import com.ctgu.oamessage.mapper.EmailMapper;
import com.ctgu.oamessage.service.core.EmailService;
import com.ctgu.oauser.service.common.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static com.ctgu.oacommon.constant.EmailConst.MAIL_BOX;
import static com.ctgu.oacommon.utils.AesUtils.decryptAes;

/**
 * @author Zhang Jinming
 * @date 20/8/2022 下午5:41
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private MailClientService mailClientService;
    @Autowired
    private EmailAccountMapper emailAccountMapper;
    @Autowired
    private EmailMapper emailMapper;
    @Autowired
    private UserCommonService userCommonService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> sendMail(MailPageVO mailPageVO) {
        try {
            Long id = UserThreadHolder.getCurrentUser().getId();
            EmailAccount emailAccount = emailAccountMapper.selectById(id);
            if (emailAccount == null) {
                throw new BizException("尚未绑定邮箱！");
            }
            mailClientService.smtpMail(EmailAccountDTO.builder()
                    .email(emailAccount.getEmail())
                    .password(decryptAes(emailAccount.getPassword()))
                    .host(emailAccount.getHost())
                    .port(emailAccount.getPort())
                    .protocol(emailAccount.getProtocol())
                    .build(), EmailDTO.builder()
                    .email(mailPageVO.getEmailAddress())
                    .subject(mailPageVO.getSubject())
                    .content(mailPageVO.getContent())
                    .build());
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
            if (insert <= 0) {
                throw new BizException("发件失败");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Result.ok();
    }

    //获取所有自己发送的邮件(发件箱)
    @Override
    public Result<PageResult<Email>> getAllEmailFromOutBox(Long id, Integer current, Integer size) {
        if (current <= 0 || size <= 0 || ObjectUtils.isEmpty(current) || ObjectUtils.isEmpty(size)) {
            List<Email> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Email> emailPageResult = new PageResult<>(listNull, 0);
            return Result.fail(emailPageResult);
        }
        Page<Email> page = new Page<>(current, size);
        QueryWrapper<Email> wrapper = new QueryWrapper<>();
        wrapper.eq("su_id", id).in("send_email_type", EmailConst.MAIL_BOX, EmailConst.OTHER_BOX);
        Page<Email> emailPage = emailMapper.selectPage(page, wrapper);
        List<Email> records = emailPage.getRecords();
        //获取发送者和接收者用户名
        if (!ObjectUtils.isEmpty(records)) {
            //发送者的获取一次就好了
            String sendUserName = userCommonService.getUserNameByUserId(records.get(0).getSuId());
            for (Email email : records) {
                //获取接收者用户名
                String acceptUserName = userCommonService.getUserNameByUserId(email.getAuId());
                email.setAcceptUserName(acceptUserName);
                email.setSendUserName(sendUserName);
            }
            PageResult<Email> userPageResult = new PageResult<>(emailPage.getRecords(), (int) emailPage.getTotal());

            return Result.ok(userPageResult);
        } else {
            List<Email> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Email> emailPageResult = new PageResult<Email>(listNull, 0);
            return Result.ok(emailPageResult);
        }
    }

    //获取所有自己接收的邮件(收件箱)
    @Override
    public Result<PageResult<Email>> getAllEmailFromInBox(Long id, Integer current, Integer size) {
        if (current <= 0 || size <= 0 || ObjectUtils.isEmpty(current) || ObjectUtils.isEmpty(size)) {
            List<Email> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Email> emailPageResult = new PageResult<>(listNull, 0);
            return Result.fail(emailPageResult);
        }
        Page<Email> page = new Page<>(current, size);
        QueryWrapper<Email> wrapper = new QueryWrapper<>();
        wrapper.eq("au_id", id).in("send_email_type", EmailConst.MAIL_BOX, EmailConst.ALETE_BOX, EmailConst.RESTORE_BOX);
        Page<Email> emailPage = emailMapper.selectPage(page, wrapper);
        List<Email> records = emailPage.getRecords();
        //获取发送者和接收者用户名
        if (!ObjectUtils.isEmpty(records)) {
            //发送者的获取一次就好了
            String sendUserName = userCommonService.getUserNameByUserId(records.get(0).getSuId());
            for (Email email : records) {
                //获取接收者用户名
                String acceptUserName = userCommonService.getUserNameByUserId(email.getAuId());
                email.setAcceptUserName(acceptUserName);
                email.setSendUserName(sendUserName);
            }
            PageResult<Email> userPageResult = new PageResult<>(emailPage.getRecords(), (int) emailPage.getTotal());

            return Result.ok(userPageResult);
        } else {
            List<Email> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Email> emailPageResult = new PageResult<Email>(listNull, 0);
            return Result.ok(emailPageResult);
        }
    }

    /**
     * 获取所有自己编写但是没有发送的邮件(草稿箱)
     */
    @Override
    public Result<PageResult<Email>> getAllEmailFromDraftBox(Long id, Integer current, Integer size) {
        if (current <= 0 || size <= 0 || ObjectUtils.isEmpty(current) || ObjectUtils.isEmpty(size)) {
            List<Email> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Email> emailPageResult = new PageResult<>(listNull, 0);
            return Result.fail(emailPageResult);
        }
        Page<Email> page = new Page<>(current, size);
        QueryWrapper<Email> wrapper = new QueryWrapper<>();
        wrapper.eq("su_id", id).eq("send_email_type", EmailConst.DRAFT_BOX);
        Page<Email> emailPage = emailMapper.selectPage(page, wrapper);
        List<Email> records = emailPage.getRecords();
        //获取发送者和接收者用户名
        if (!ObjectUtils.isEmpty(records)) {
            //发送者的获取一次就好了
            String sendUserName = userCommonService.getUserNameByUserId(records.get(0).getSuId());
            for (Email email : records) {
                //获取接收者用户名
                String acceptUserName = userCommonService.getUserNameByUserId(email.getAuId());
                email.setAcceptUserName(acceptUserName);
                email.setSendUserName(sendUserName);
            }
            PageResult<Email> userPageResult = new PageResult<>(emailPage.getRecords(), (int) emailPage.getTotal());

            return Result.ok(userPageResult);
        } else {
            List<Email> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Email> emailPageResult = new PageResult<Email>(listNull, 0);
            return Result.ok(emailPageResult);
        }
    }

    /**
     * 获取所有自己删除的的邮件(垃圾箱)
     * 这个里面只保存别人发给自己，自己又删除的邮件，
     * 而对于自己发送出去的消息或者删除自己编写到草稿箱里面的邮件删除后都不会进入回收站
     */
    @Override
    public Result<PageResult<Email>> getAllEmailFromGarbageBox(Long id, Integer current, Integer size) {
        if (current <= 0 || size <= 0 || ObjectUtils.isEmpty(current) || ObjectUtils.isEmpty(size)) {
            List<Email> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Email> emailPageResult = new PageResult<>(listNull, 0);
            return Result.fail(emailPageResult);
        }
        Page<Email> page = new Page<>(current, size);
        QueryWrapper<Email> wrapper = new QueryWrapper<>();
        wrapper.eq("au_id", id).eq("accept_email_type", EmailConst.BIN_BOX);
        Page<Email> emailPage = emailMapper.selectPage(page, wrapper);
        List<Email> records = emailPage.getRecords();
        //获取发送者和接收者用户名
        if (!ObjectUtils.isEmpty(records)) {
            //发送者的获取一次就好了
            String sendUserName = userCommonService.getUserNameByUserId(records.get(0).getSuId());
            for (Email email : records) {
                //获取接收者用户名
                String acceptUserName = userCommonService.getUserNameByUserId(email.getAuId());
                email.setAcceptUserName(acceptUserName);
                email.setSendUserName(sendUserName);
            }
            PageResult<Email> userPageResult = new PageResult<>(emailPage.getRecords(), (int) emailPage.getTotal());

            return Result.ok(userPageResult);
        } else {
            List<Email> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Email> emailPageResult = new PageResult<Email>(listNull, 0);
            return Result.ok(emailPageResult);
        }
    }

    //删除对应id的邮件（删除收件箱中的邮件时调用的方法,实际是更新操作）
    @Override
    public Result<Boolean> deleteEmailToGarbageBoxById(Long id) {
        QueryWrapper<Email> wrapper = new QueryWrapper<Email>();
        wrapper.eq("id", id);
        Email email = emailMapper.selectOne(wrapper);
        email.setAcceptEmailType(EmailConst.BIN_BOX);
        email.setSendEmailType(EmailConst.OTHER_BOX);
        int update = emailMapper.updateById(email);
        if (update < 0) {
            return Result.fail(false);
        }
        return Result.ok(true);
    }

    //彻底删除对应id邮件（将垃圾箱里面的邮件彻底删除）
    @Override
    public Result<Boolean> deleteAleteEmailFromGarbageBoxById(Long id) {
        QueryWrapper<Email> wrapper = new QueryWrapper<Email>();
        wrapper.eq("id", id);
        Email email = emailMapper.selectOne(wrapper);
        email.setAcceptEmailType(EmailConst.ALETE_BOX);
        int update = emailMapper.updateById(email);
        if (update < 0) {
            return Result.fail(false);
        }
        return Result.ok(true);
    }

    //删除自己发送的邮件还有自己写的草稿会执行这个方法这个也是彻底删除
    @Override
    public Result<Boolean> deleteEmailFromBox(Long id) {
        QueryWrapper<Email> wrapper = new QueryWrapper<Email>();
        wrapper.eq("id", id);
        Email email = emailMapper.selectOne(wrapper);
        email.setSendEmailType(EmailConst.ALETE_BOX);
        int update = emailMapper.updateById(email);
        if (update < 0) {
            return Result.fail(false);
        }
        return Result.ok(true);
    }

    //从垃圾箱中恢复对应id的邮件
    @Override
    public Result<Boolean> restoreEmailFromGarbageBox(Long id) {
        QueryWrapper<Email> wrapper = new QueryWrapper<Email>();
        wrapper.eq("id", id);
        Email email = emailMapper.selectOne(wrapper);
        email.setAcceptEmailType(EmailConst.MAIL_BOX);
        email.setSendEmailType(EmailConst.RESTORE_BOX);
        int update = emailMapper.updateById(email);
        if (update < 0) {
            return Result.fail(false);
        }
        return Result.ok(true);
    }

}
