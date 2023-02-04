package com.ctgu.message.service.core.impl;

import com.ctgu.common.bo.EmailAccountBO;
import com.ctgu.common.bo.EmailBO;
import com.ctgu.common.constants.RedisPrefixConst;
import com.ctgu.common.dao.message.EmailAccountMapper;
import com.ctgu.common.dao.user.UserInfoMapper;
import com.ctgu.common.entity.EmailAccount;
import com.ctgu.common.entity.UserInfo;
import com.ctgu.common.exception.BizException;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.EmailAccountVO;
import com.ctgu.common.service.mail.MailClientService;
import com.ctgu.common.utils.CommonUtils;
import com.ctgu.common.utils.ThreadHolder;
import com.ctgu.message.service.core.EmailAccountService;
import com.ctgu.redis.service.RedisService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import static com.ctgu.common.constants.EmailAccountConst.*;
import static com.ctgu.common.constants.RedisPrefixConst.CODE_EXPIRE_TIME;
import static com.ctgu.common.utils.AesUtils.encryptAes;

/**
 * @author Zhang Jinming
 * @date 19/8/2022 下午5:12
 */
@Log4j2
@Service
public class EmailAccountServiceImpl implements EmailAccountService {
    @Resource
    private EmailAccountMapper emailAccountMapper;

    @Resource
    private MailClientService mailClientService;

    @Resource
    private RedisService redisService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public Result<?> registerEmailAccount(EmailAccountVO emailAccountVO) {
        if (!emailAccountVO.getCode().equals(redisService.get(RedisPrefixConst.USER_CODE_KEY + emailAccountVO.getEmail()))) {
            throw new BizException("验证码错误！");
        }
        EmailAccount emailAccount = EmailAccount.builder()
                .id(ThreadHolder.getCurrentUser().getId())
                .email(emailAccountVO.getEmail())
                .password(encryptAes(emailAccountVO.getPassword()))
                .build();
        if (emailAccountVO.getUrl().equals(MAIL_SERVER_QQ)) {
            emailAccount.setHost(MAIL_SERVER_HOST_QQ);
            emailAccount.setPort(MAIL_SERVER_PORT_QQ);
            emailAccount.setProtocol(MAIL_SERVER_PROTOCOL_QQ);
        } else {
            emailAccount.setHost(MAIL_SERVER_HOST_163);
            emailAccount.setPort(MAIL_SERVER_PORT_163);
            emailAccount.setProtocol(MAIL_SERVER_PROTOCOL_163);
        }
        int insert = emailAccountMapper.insert(emailAccount);
        if (insert <= 0) {
            throw new BizException("插入失败！");
        }
        userInfoMapper.updateById(UserInfo.builder()
                .id(ThreadHolder.getCurrentUser().getId())
                .email(emailAccountVO.getEmail())
                .build());
        return Result.ok();
    }

    @Override
    public void sendCode(EmailAccountVO emailAccountVO) {
        if (CommonUtils.isUnValidEmail(emailAccountVO.getEmail())) {
            throw new BizException("请输入正确邮箱");
        }
        EmailAccountBO emailAccount = EmailAccountBO.builder()
                .email(emailAccountVO.getEmail())
                .password(emailAccountVO.getPassword())
                .build();
        if (emailAccountVO.getUrl().equals(MAIL_SERVER_QQ)) {
            emailAccount.setHost(MAIL_SERVER_HOST_QQ);
            emailAccount.setPort(MAIL_SERVER_PORT_QQ);
            emailAccount.setProtocol(MAIL_SERVER_PROTOCOL_QQ);
        } else {
            emailAccount.setHost(MAIL_SERVER_HOST_163);
            emailAccount.setPort(MAIL_SERVER_PORT_163);
            emailAccount.setProtocol(MAIL_SERVER_PROTOCOL_163);
        }
        String code = CommonUtils.getRandomCode();
        log.info("已生成验证码：" + code);
        int deadline = (int) CODE_EXPIRE_TIME / 60;
        log.info("已设置验证码的过期时间为：" + deadline + "min");
        try {
            mailClientService.smtpMail(emailAccount, EmailBO.builder()
                    .email(emailAccountVO.getEmail())
                    .subject("【AutoOffice】开发者测试邮件")
                    .content("您正在进行邮箱客户端绑定操作<br>" +
                            "您的验证码为 " + code + " 有效期" + deadline + "分钟，请不要告诉他人哦！<br>" +
                            "如果您不清楚这封邮件的来源，非常抱歉我们误发了邮件<br>" +
                            "我们正在进行项目测试，如果您反复收到此邮件，烦请通过此邮箱与我们联系")
                    .build());
        } catch (MessagingException e) {
            throw new RuntimeException("邮件线程启动失败");
        }
        redisService.set(RedisPrefixConst.USER_CODE_KEY + emailAccountVO.getEmail(), code, CODE_EXPIRE_TIME);
    }

    @Override
    public Result<?> checkExist() {
        EmailAccount emailAccount = emailAccountMapper.selectById(ThreadHolder.getCurrentUser().getId());
        return emailAccount == null ? Result.fail("邮箱不存在") : Result.ok(emailAccount.getEmail());
    }
}
