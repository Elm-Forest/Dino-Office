package com.ctgu.user.service.core.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctgu.common.bo.RsaBO;
import com.ctgu.common.bo.TokenBO;
import com.ctgu.common.constants.RedisPrefixConst;
import com.ctgu.common.dao.user.UserInfoMapper;
import com.ctgu.common.dao.user.UserMapper;
import com.ctgu.common.entity.User;
import com.ctgu.common.entity.UserInfo;
import com.ctgu.common.enums.StatusCodeEnum;
import com.ctgu.common.exception.BizException;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.AuthVO;
import com.ctgu.common.models.vo.UserVO;
import com.ctgu.common.utils.ThreadHolder;
import com.ctgu.redis.service.RedisService;
import com.ctgu.user.service.common.UserCommonService;
import com.ctgu.user.service.core.AuthService;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.ctgu.common.constants.CommonConst.ACTIVATED;
import static com.ctgu.common.constants.CommonConst.NO_ENTRY;
import static com.ctgu.common.constants.RedisPrefixConst.USER_AUTH_KEY;
import static com.ctgu.common.constants.RoleConst.FRESHMAN_RIGHTS;
import static com.ctgu.common.constants.RoleConst.UN_EMPLOYED;
import static com.ctgu.common.utils.JwtUtils.createToken;
import static com.ctgu.common.utils.Md5Utils.passwordMd5;
import static java.util.Objects.nonNull;

/**
 * @author Zhang Jinming
 * @date 15/1/2023 下午8:36
 */
@Log4j2
@Service
public class AuthServiceImpl implements AuthService {
    @Resource
    private RedisService redisService;

    @Resource
    private UserCommonService userCommonService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private ModelMapper modelMapper;

    @Override
    public Result<?> login(AuthVO auth) {
        String msg;
        User user = userCommonService.selectUserByUserName(auth.getUsername());
        try {
            if (!nonNull(user)) {
                throw new BizException("账户不存在");
            }
            userCommonService.checkActivated(user.getStatus());
            redisService.set(USER_AUTH_KEY + user.getId(), user, 15);
            ThreadHolder.addCurrentUser(user);
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), auth.getPassword());
            SecurityUtils.getSubject().login(usernamePasswordToken);
            TokenBO payload = modelMapper.map(user, TokenBO.class);
            String token = createToken(BeanUtil.beanToMap(payload));
            redisService.set(RedisPrefixConst.TOKEN_KEY + user.getId(), token);
            userMapper.update(User.builder().lastTime(DateUtil.date()).build(),
                    new LambdaQueryWrapper<User>().eq(User::getId, user.getId()));
            return Result.ok(token);
        } catch (UnknownAccountException e) {
            log.error(e.getMessage());
            msg = "账户不存在";
        } catch (IncorrectCredentialsException e) {
            log.error(e.getMessage());
            msg = "密码有误";
        } catch (BizException e) {
            msg = e.getMessage();
        }
        throw new BizException(msg);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> register(UserVO userVO) {
        int insert;
        if (userCommonService.checkUser(userVO)) {
            throw new BizException("邮箱或用户名已被注册！ ");
        }
        RsaBO rsa = passwordMd5(userVO.getPassword());
        User user = User.builder()
                .username(userVO.getUsername())
                .password(rsa.getPassword())
                .salt(rsa.getSalt())
                .email(userVO.getEmail())
                .status(ACTIVATED)
                .regTime(DateUtil.date())
                .role(UN_EMPLOYED)
                .rights(FRESHMAN_RIGHTS)
                .build();
        insert = userMapper.insert(user);
        if (insert <= 0) {
            throw new RuntimeException("注册失败！");
        }
        UserInfo userInfo = UserInfo.builder()
                .id(user.getId())
                .status(NO_ENTRY)
                .build();
        insert = userInfoMapper.insert(userInfo);
        if (insert <= 0) {
            throw new RuntimeException("注册失败！");
        }
        return Result.ok();
    }

    @Override
    public Result<?> logout() {
        SecurityUtils.getSubject().logout();
        redisService.del(RedisPrefixConst.TOKEN_KEY + ThreadHolder.getCurrentUser().getId());
        redisService.del(USER_AUTH_KEY + ThreadHolder.getCurrentUser().getId());
        return Result.fail(StatusCodeEnum.TIMEOUT, "已成功登出！");
    }

    @Override
    public void sendCode(String email) {
        userCommonService.sendCode(email, "账户注册");
    }

}
