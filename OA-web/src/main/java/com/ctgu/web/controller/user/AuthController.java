package com.ctgu.web.controller.user;

import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.AuthVO;
import com.ctgu.common.models.vo.UserVO;
import com.ctgu.user.service.core.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Zhang Jinming
 * @date 15/8/2022 下午9:51
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
