package com.dino.user.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dino.common.bo.EmailBO;
import com.dino.common.constants.RedisPrefixConst;
import com.dino.common.dao.user.UserInfoMapper;
import com.dino.common.dao.user.UserMapper;
import com.dino.common.entity.User;
import com.dino.common.entity.UserInfo;
import com.dino.common.exception.BizException;
import com.dino.common.models.vo.UserInfoVO;
import com.dino.common.models.vo.UserVO;
import com.dino.common.service.mail.MailService;
import com.dino.common.utils.CommonUtils;
import com.dino.redis.service.RedisService;
import com.dino.user.service.common.UserCommonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;

import static com.dino.common.constants.CommonConst.ACTIVATED;
import static com.dino.common.constants.RedisPrefixConst.CODE_EXPIRE_TIME;
import static java.util.Objects.nonNull;

/**
 * @author Zhang Jinming
 */
@Log4j2
@Service
public class UserCommonServiceImpl implements UserCommonService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisService redisService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private MailService mailService;

    @Override
    public User selectUserByUserName(String username) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public void checkActivated(Integer status) {
        if (status != ACTIVATED) {
            throw new BizException("账号已失效或未激活");
        }
    }

    @Override
    public Boolean checkUser(UserVO userVO) {
        if (!userVO.getCode().equals(redisService.get(RedisPrefixConst.USER_CODE_KEY + userVO.getEmail()))) {
            throw new BizException("验证码错误！");
        }
        User email = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getEmail)
                .eq(User::getEmail, userVO.getEmail()));
        User username = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername)
                .eq(User::getUsername, userVO.getUsername()));
        return (nonNull(email) || nonNull(username));
    }

    @Override
    public int updateUserInfoById(UserInfoVO user) {
        return userInfoMapper.updateById(UserInfo.builder()
                .id(user.getId())
                .name(user.getName())
                .sex(user.getSex())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build());
    }

    @Override
    public int insertUserInfoById(UserInfoVO user) {
        return userInfoMapper.insert(UserInfo.builder()
                .id(user.getId())
                .name(user.getName())
                .deptId(user.getDeptId())
                .sex(user.getSex())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build());
    }

    @Override
    public void sendCode(String email, String msg) {
        if (CommonUtils.isUnValidEmail(email)) {
            throw new BizException("请输入正确邮箱");
        }
        String code = CommonUtils.getRandomCode();
        log.info("已生成验证码：" + code);
        int deadline = (int) CODE_EXPIRE_TIME / 60;
        log.info("已设置验证码的过期时间为：" + deadline + "min");
        EmailBO emailDTO = EmailBO.builder()
                .email(email)
                .subject("【AutoOffice】开发者测试邮件")
                .content("您正在进行" + msg + "操作<br>" +
                        "您的验证码为 " + code + " 有效期" + deadline + "分钟，请不要告诉他人哦！<br>" +
                        "如果您不清楚这封邮件的来源，非常抱歉我们误发了邮件<br>" +
                        "我们正在进行项目测试，如果您反复收到此邮件，烦请通过此邮箱与我们联系")
                .build();
        try {
            log.info("邮件线程正在启动");
            mailService.sendMail(emailDTO);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        redisService.set(RedisPrefixConst.USER_CODE_KEY + email, code, CODE_EXPIRE_TIME);
    }

    @Override
    public List<User> getUserIdByUserName(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("username", username);
        return userMapper.selectList(wrapper);
    }

    @Override
    public String getUserNameByUserId(Long id) {
        return userMapper.getUserNameByUserId(id);
    }
}
