package com.ctgu.oaweb.controller.user;

import com.ctgu.oacommon.vo.Result;
import com.ctgu.oacommon.vo.UserVO;
import com.ctgu.oauser.service.core.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zhang Jinming
 * @create 10/8/2022 下午9:56
 */
@RestController
@Api(tags = "用户管理")
public class UserController {
    @Autowired
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
