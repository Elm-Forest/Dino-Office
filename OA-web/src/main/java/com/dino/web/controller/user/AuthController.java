package com.dino.web.controller.user;

import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.AuthVO;
import com.dino.common.models.vo.UserVO;
import com.dino.user.service.core.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Zhang Jinming
 */
@Api(tags = "用户认证接口")
@RestController
public class AuthController {
    @Resource
    private AuthService authService;

    @PostMapping("/login")
    public Result<?> login(AuthVO authVO) {
        return authService.login(authVO);
    }

    @PostMapping("/register")
    public Result<?> register(UserVO user) {
        return authService.register(user);
    }

    @ApiOperation(value = "发送邮件")
    @PostMapping("/email/register")
    public void sendCode(@RequestParam("email") String email) {
        authService.sendCode(email);
    }

    @PostMapping("/user/logout")
    @ApiOperation(value = "登出")
    public Result<?> logout() {
        return authService.logout();
    }
}
