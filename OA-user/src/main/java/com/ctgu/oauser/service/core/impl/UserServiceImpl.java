package com.ctgu.oauser.service.core.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctgu.oacommon.constant.RedisPrefixConst;
import com.ctgu.oacommon.dto.RsaDTO;
import com.ctgu.oacommon.entity.User;
import com.ctgu.oacommon.exception.BizException;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oacommon.vo.UserVO;
import com.ctgu.oaredis.service.RedisService;
import com.ctgu.oauser.mapper.UserMapper;
import com.ctgu.oauser.service.common.UserCommonService;
import com.ctgu.oauser.service.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ctgu.oacommon.utils.Md5Utils.passwordMd5;

/**
 * @author Zhang Jinming
 * &#064;create  10/8/2022 下午10:06
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCommonService userCommonService;

    @Autowired
    private RedisService redisService;

    @Override
    public Result<?> selectUser() {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId, User::getEmail, User::getRole, User::getRights, User::getRegTime, User::getLastTime)
                .eq(User::getId, UserThreadHolder.getCurrentUser().getId()));
        return Result.ok(user);
    }

    @Override
    public Result<?> forgetPassword(UserVO userVO) {
        try {
            if (!userCommonService.checkUser(userVO)) {
                throw new BizException("邮箱不存在！ ");
            }
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .select(User::getId).eq(User::getEmail, userVO.getEmail()));
            RsaDTO rsaDTO = passwordMd5(userVO.getPassword());
            user.setPassword(rsaDTO.getPassword());
            user.setSalt(rsaDTO.getSalt());
            int update = userMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getId, user.getId()));
            if (update <= 0) {
                throw new BizException("更新失败!");
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @Override
    public void sendCode(String email) {
        userCommonService.sendCode(email, "找回密码");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> updatePassword(String newPassword, String code) {
        try {
            User user = userMapper.selectById(UserThreadHolder.getCurrentUser().getId());
            if (!code.equals(redisService.get(RedisPrefixConst.USER_CODE_KEY + user.getEmail()))) {
                throw new BizException("验证码错误！");
            }
            RsaDTO rsaDTO = passwordMd5(newPassword);
            int update = userMapper.updateById(User.builder()
                    .id(user.getId())
                    .password(rsaDTO.getPassword())
                    .salt(rsaDTO.getSalt())
                    .build());
            if (update <= 0) {
                throw new BizException("更新失败！");
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }

        return Result.ok();
    }

    @Override
    public void autoSendCode() {
        User user = userMapper.selectById(UserThreadHolder.getCurrentUser().getId());
        userCommonService.sendCode(user.getEmail(), "重置密码");
    }

}
