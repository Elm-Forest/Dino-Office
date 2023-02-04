package com.ctgu.web.controller.user;

import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.UserVO;
import com.ctgu.user.service.core.UserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Zhang Jinming
 * @create 10/8/2022 下午9:56
 */
@Api(tags = "用户认证信息管理")
@RestController
public class AuthInfoController {
    @Resource
    private UserService userService;

    @GetMapping("/user/user")
    public Result<?> selectUser() {
        return userService.selectUser();
    }

    @PutMapping("/user/user")
    public Result<?> updatePassword(@RequestParam("newPassword") String password,
                                    @RequestParam("code") String code) {
        return userService.updatePassword(password, code);
    }

    @PostMapping("/user/user/code")
    public void sendCode() {
        userService.autoSendCode();
    }

    @PostMapping("/password/back")
    public Result<?> forgetPassword(UserVO userVO) {
        return userService.forgetPassword(userVO);
    }

    @PostMapping("/email/password")
    public Result<?> sendCode(String email) {
        userService.sendCode(email);
        return Result.ok();
    }
}
