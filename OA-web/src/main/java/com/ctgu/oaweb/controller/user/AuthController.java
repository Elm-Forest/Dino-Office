package com.ctgu.oaweb.controller.user;

import com.ctgu.oacommon.vo.AuthVO;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oacommon.vo.UserVO;
import com.ctgu.oauser.service.core.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhang Jinming
 * @date 15/8/2022 下午9:51
 */
@Api("User认证接口，仅限用户本人操作，登录与注册")
@RestController
public class AuthController {
    @Autowired
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
