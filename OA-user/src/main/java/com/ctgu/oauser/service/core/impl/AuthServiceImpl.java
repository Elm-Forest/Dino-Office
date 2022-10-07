package com.ctgu.oauser.service.core.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctgu.oacommon.constant.RedisPrefixConst;
import com.ctgu.oacommon.dto.RsaDTO;
import com.ctgu.oacommon.dto.TokenDTO;
import com.ctgu.oacommon.entity.User;
import com.ctgu.oacommon.entity.UserInfo;
import com.ctgu.oacommon.enums.StatusCodeEnum;
import com.ctgu.oacommon.exception.BizException;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oacommon.vo.AuthVO;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oacommon.vo.UserVO;
import com.ctgu.oaredis.service.RedisService;
import com.ctgu.oauser.mapper.UserInfoMapper;
import com.ctgu.oauser.mapper.UserMapper;
import com.ctgu.oauser.service.common.UserCommonService;
import com.ctgu.oauser.service.core.AuthService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ctgu.oacommon.constant.CommonConst.ACTIVATED;
import static com.ctgu.oacommon.constant.CommonConst.NO_ENTRY;
import static com.ctgu.oacommon.constant.RedisPrefixConst.USER_AUTH_KEY;
import static com.ctgu.oacommon.constant.RoleConst.FRESHMAN_RIGHTS;
import static com.ctgu.oacommon.constant.RoleConst.UN_EMPLOYED;
import static com.ctgu.oacommon.utils.JwtUtils.createObjectToken;
import static com.ctgu.oacommon.utils.Md5Utils.passwordMd5;
import static java.util.Objects.nonNull;

/**
 * @author Zhang Jinming
 * @date 15/8/2022 下午8:36
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private RedisService redisService;

    @Autowired
    private UserCommonService userCommonService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public Result<?> login(AuthVO auth) {
        User user = userCommonService.selectUserByUserName(auth.getUsername());
        try {
            if (!nonNull(user.getUsername())) {
                throw new BizException("账户不存在");
            }
            userCommonService.checkActivated(user.getStatus());
            redisService.set(USER_AUTH_KEY + user.getId(), user, 15);
            UserThreadHolder.addCurrentUser(user);
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), auth.getPassword());
            SecurityUtils.getSubject().login(usernamePasswordToken);
            String token = createObjectToken(TokenDTO.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .rights(user.getRights())
                    .role(user.getRole())
                    .build());
            redisService.set(RedisPrefixConst.TOKEN_KEY + user.getId(), token);
            userMapper.update(User.builder().lastTime(DateUtil.date()).build(),
                    new LambdaQueryWrapper<User>().eq(User::getId, user.getId()));
            return Result.ok(token);
        } catch (UnknownAccountException e) {
            System.out.println(e.getMessage());
            return Result.fail("账户不存在");
        } catch (IncorrectCredentialsException e) {
            System.out.println(e.getMessage());
            return Result.fail("密码有误");
        } catch (BizException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.fail(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> register(UserVO userVO) {
        int insert;
        try {
            if (userCommonService.checkUser(userVO)) {
                throw new BizException("邮箱或用户名已被注册！ ");
            }
            RsaDTO rsaDTO = passwordMd5(userVO.getPassword());
            User user = User.builder()
                    .username(userVO.getUsername())
                    .password(rsaDTO.getPassword())
                    .salt(rsaDTO.getSalt())
                    .email(userVO.getEmail())
                    .status(ACTIVATED)
                    .regTime(DateUtil.date())
                    .role(UN_EMPLOYED)
                    .rights(FRESHMAN_RIGHTS)
                    .build();
            insert = userMapper.insert(user);
            if (insert <= 0) {
                throw new BizException("插入失败！");
            }
            UserInfo userInfo = UserInfo.builder()
                    .id(user.getId())
                    .status(NO_ENTRY)
                    .build();
            insert = userInfoMapper.insert(userInfo);
            if (insert <= 0) {
                throw new BizException("插入失败！");
            }
        } catch (BizException e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @Override
    public Result<?> logout() {
        SecurityUtils.getSubject().logout();
        redisService.del(RedisPrefixConst.TOKEN_KEY + UserThreadHolder.getCurrentUser().getId());
        redisService.del(USER_AUTH_KEY + UserThreadHolder.getCurrentUser().getId());
        return Result.fail(StatusCodeEnum.TIMEOUT, "已成功登出！");
    }

    @Override
    public void sendCode(String email) {
        userCommonService.sendCode(email, "账户注册");
    }

}
