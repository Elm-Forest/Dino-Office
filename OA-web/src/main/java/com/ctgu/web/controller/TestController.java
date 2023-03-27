package com.ctgu.web.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctgu.common.bo.TokenBO;
import com.ctgu.common.constants.RedisPrefixConst;
import com.ctgu.common.dao.user.UserMapper;
import com.ctgu.common.entity.User;
import com.ctgu.common.exception.BizException;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.AuthVO;
import com.ctgu.common.utils.ThreadHolder;
import com.ctgu.redis.service.RedisService;
import com.ctgu.user.service.common.UserCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Map;

import static com.ctgu.common.constants.JwtConst.*;
import static com.ctgu.common.constants.RedisPrefixConst.USER_AUTH_KEY;
import static java.util.Objects.nonNull;

/**
 * @author Zhang Jinming
 * @date 15/8/2022 下午4:06
 */
@Api(tags = "测试接口")
@Log4j2
@RestController
public class TestController {
    @Resource
    private RedisService redisService;

    @Resource
    private UserCommonService userCommonService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ModelMapper modelMapper;

    @ApiOperation("颁发永久token令牌")
    @GetMapping("/test/token")
    public Result<?> createUserTokenCase(AuthVO auth) {
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
            Map<String, Object> map = BeanUtil.beanToMap(payload);
            JWTCreator.Builder jwt = JWT.create();
            jwt.withPayload(map);
            Calendar instance = Calendar.getInstance();
            instance.add(CALENDAR_FIELD, CALENDAR_INTERVAL * 1000);
            jwt.withExpiresAt(instance.getTime());
            String token = jwt.sign(Algorithm.HMAC256(PRIVATE_KEY));
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
}
