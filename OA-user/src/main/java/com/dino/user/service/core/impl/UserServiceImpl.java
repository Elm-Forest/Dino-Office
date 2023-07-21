package com.dino.user.service.core.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dino.common.bo.RsaBO;
import com.dino.common.constants.RedisPrefixConst;
import com.dino.common.dao.user.UserMapper;
import com.dino.common.entity.User;
import com.dino.common.exception.BizException;
import com.dino.common.models.dto.AuthInfoDTO;
import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.UserVO;
import com.dino.common.utils.Assert;
import com.dino.common.utils.ThreadHolder;
import com.dino.redis.service.RedisService;
import com.dino.user.service.common.UserCommonService;
import com.dino.user.service.core.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.dino.common.utils.Md5Utils.passwordMd5;

/**
 * @author Zhang Jinming
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserCommonService userCommonService;

    @Resource
    private RedisService redisService;

    @Resource
    private ModelMapper modelMapper;

    @Override
    public Result<?> selectUser() {
        User userEntity = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, ThreadHolder.getCurrentUser().getId()));
        AuthInfoDTO authInfo = modelMapper.map(userEntity, AuthInfoDTO.class);
        return Result.ok(authInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> forgetPassword(UserVO userVO) {
        if (!userCommonService.checkUser(userVO)) {
            throw new BizException("邮箱不存在");
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId).eq(User::getEmail, userVO.getEmail()));
        RsaBO rsaDTO = passwordMd5(userVO.getPassword());
        user.setPassword(rsaDTO.getPassword());
        user.setSalt(rsaDTO.getSalt());
        int update = userMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getId, user.getId()));
        if (update <= 0) {
            throw new RuntimeException("更新失败");
        }
        return Result.ok();
    }

    @Override
    public void sendPasswordCode(String email) {
        userCommonService.sendCode(email, "找回密码");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> updatePassword(String newPassword, String code) {
        User user = userMapper.selectById(ThreadHolder.getCurrentUser().getId());
        if (!code.equals(redisService.get(RedisPrefixConst.USER_CODE_KEY + user.getEmail()))) {
            throw new BizException("验证码错误！");
        }
        RsaBO rsaDTO = passwordMd5(newPassword);
        int update = userMapper.updateById(User.builder()
                .id(user.getId())
                .password(rsaDTO.getPassword())
                .salt(rsaDTO.getSalt())
                .build());
        Assert.greaterThanZero(update, new RuntimeException("修改失败"));
        return Result.ok();
    }

    @Override
    public void autoSendCode() {
        User user = userMapper.selectById(ThreadHolder.getCurrentUser().getId());
        userCommonService.sendCode(user.getEmail(), "重置密码");
    }

    @Override
    public Result<?> updateEmail(String email, String code) {
        User user = userMapper.selectById(ThreadHolder.getCurrentUser().getId());
        if (!code.equals(redisService.get(RedisPrefixConst.USER_CODE_KEY + email))) {
            throw new BizException("验证码错误！");
        }
        int update = userMapper.updateById(User.builder()
                .id(user.getId())
                .email(email)
                .build());
        Assert.greaterThanZero(update, new RuntimeException("修改失败"));
        return Result.ok();
    }

    @Override
    public void sendEmailCode(String email) {
        userCommonService.sendCode(email, "修改邮箱");
    }
}
