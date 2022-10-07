package com.ctgu.oamessage.service.core.impl;

import com.ctgu.oacommon.constant.RedisPrefixConst;
import com.ctgu.oacommon.dto.EmailAccountDTO;
import com.ctgu.oacommon.dto.EmailDTO;
import com.ctgu.oacommon.entity.EmailAccount;
import com.ctgu.oacommon.entity.UserInfo;
import com.ctgu.oacommon.exception.BizException;
import com.ctgu.oacommon.service.mailService.MailClientService;
import com.ctgu.oacommon.utils.CommonUtils;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oacommon.vo.EmailAccountVO;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oamessage.mapper.EmailAccountMapper;
import com.ctgu.oamessage.service.core.EmailAccountService;
import com.ctgu.oaredis.service.RedisService;
import com.ctgu.oauser.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

import static com.ctgu.oacommon.constant.EmailAccountConst.*;
import static com.ctgu.oacommon.constant.RedisPrefixConst.CODE_EXPIRE_TIME;
import static com.ctgu.oacommon.utils.AesUtils.encryptAes;

/**
 * @author Zhang Jinming
 * @date 19/8/2022 下午5:12
 */
@Service
public class EmailAccountServiceImpl implements EmailAccountService {
    @Autowired
    private EmailAccountMapper emailMapper;

    @Autowired
    private MailClientService mailClientService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Result<?> registerEmailAccount(EmailAccountVO emailAccountVO) {
        try {
            if (!emailAccountVO.getCode().equals(redisService.get(RedisPrefixConst.USER_CODE_KEY + emailAccountVO.getEmail()))) {
                throw new BizException("验证码错误！");
            }
            EmailAccount emailAccount = EmailAccount.builder()
                    .id(UserThreadHolder.getCurrentUser().getId())
                    .email(emailAccountVO.getEmail())
                    .password(encryptAes(emailAccountVO.getPassword()))
                    .build();
            if (emailAccountVO.getType().equals(MAIL_SERVER_QQ)) {
                emailAccount.setHost(MAIL_SERVER_HOST_QQ);
                emailAccount.setPort(MAIL_SERVER_PORT_QQ);
                emailAccount.setProtocol(MAIL_SERVER_PROTOCOL_QQ);
            } else {
                emailAccount.setHost(MAIL_SERVER_HOST_163);
                emailAccount.setPort(MAIL_SERVER_PORT_163);
                emailAccount.setProtocol(MAIL_SERVER_PROTOCOL_163);
            }
            int insert = emailMapper.insert(emailAccount);
            if (insert <= 0) {
                throw new BizException("插入失败！");
            }
            userInfoMapper.updateById(UserInfo.builder()
                    .id(UserThreadHolder.getCurrentUser().getId())
                    .email(emailAccountVO.getEmail())
                    .build());
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @Override
    public void sendCode(EmailAccountVO emailAccountVO) {
        if (!CommonUtils.checkEmail(emailAccountVO.getEmail())) {
            throw new BizException("请输入正确邮箱");
        }
        EmailAccountDTO emailAccount = EmailAccountDTO.builder()
                .email(emailAccountVO.getEmail())
                .password(emailAccountVO.getPassword())
                .build();
        if (emailAccountVO.getType().equals(MAIL_SERVER_QQ)) {
            emailAccount.setHost(MAIL_SERVER_HOST_QQ);
            emailAccount.setPort(MAIL_SERVER_PORT_QQ);
            emailAccount.setProtocol(MAIL_SERVER_PROTOCOL_QQ);
        } else {
            emailAccount.setHost(MAIL_SERVER_HOST_163);
            emailAccount.setPort(MAIL_SERVER_PORT_163);
            emailAccount.setProtocol(MAIL_SERVER_PROTOCOL_163);
        }
        String code = CommonUtils.getRandomCode();
        System.out.println("已生成验证码：" + code);
        int deadline = (int) CODE_EXPIRE_TIME / 60;
        System.out.println("已设置验证码的过期时间为：" + deadline + "min");
        try {
            mailClientService.smtpMail(emailAccount, EmailDTO.builder()
                    .email(emailAccountVO.getEmail())
                    .subject("【AutoOffice】开发者测试邮件")
                    .content("您正在进行邮箱客户端绑定操作<br>" +
                            "您的验证码为 " + code + " 有效期" + deadline + "分钟，请不要告诉他人哦！<br>" +
                            "如果您不清楚这封邮件的来源，非常抱歉我们误发了邮件<br>" +
                            "我们正在进行项目测试，如果您反复收到此邮件，烦请通过此邮箱与我们联系")
                    .build());
        } catch (MessagingException e) {
            System.out.println("邮件线程正在启动");
            throw new RuntimeException(e);
        }
        redisService.set(RedisPrefixConst.USER_CODE_KEY + emailAccountVO.getEmail(), code, CODE_EXPIRE_TIME);
    }

    @Override
    public Result<?> checkExist() {
        EmailAccount emailAccount = emailMapper.selectById(UserThreadHolder.getCurrentUser().getId());
        return emailAccount == null ? Result.fail() : Result.ok(emailAccount.getEmail());
    }


}
