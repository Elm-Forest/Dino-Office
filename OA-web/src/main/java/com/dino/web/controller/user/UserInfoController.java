package com.dino.web.controller.user;

import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.UserInfoVO;
import com.dino.department.service.DepartmentService;
import com.dino.user.service.core.UserInfoService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author Zhang Jinming
 */
@Api(tags = "用户个人信息管理")
@RestController
@RequestMapping(value = "/user")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @Resource
    private DepartmentService departmentService;

    @PostMapping("/img")
    public Result<?> uploadHeadImage(@RequestParam("file") MultipartFile headImg) {
        return userInfoService.updateUserHeadImg(headImg);
    }

    @GetMapping({"/img"})
    public Result<?> downloadUserImage() {
        return userInfoService.selectUserHeadImg();
    }

    @GetMapping("/userinfo")
    public Result<?> selectUserInfo() {
        return userInfoService.selectUserInfo();
    }

    @PostMapping("/userinfo")
    public Result<?> insertUserInfo(UserInfoVO userInfoVO) {
        return userInfoService.insertUserInfo(userInfoVO);
    }

    @PutMapping("/userinfo")
    public Result<?> updateUserInfo(UserInfoVO userInfoVO) {
        return userInfoService.updateUserInfo(userInfoVO);
    }

    @DeleteMapping("/userinfo")
    public Result<?> deleteUserInfo() {
        return Result.fail("接口不可用！");
    }

    @PostMapping("/userinfo/dept")
    public Result<?> entryApply(@RequestParam("deptId") Long deptId) {
        return userInfoService.entryApply(deptId);
    }

    @GetMapping("/dept")
    public Result<?> selectDepartmentList(@RequestParam("name") String name) {
        return departmentService.selectDepartmentByName(name);
    }

    @GetMapping("/entry")
    public Result<?> checkEntry() {
        return userInfoService.checkEntry();
    }

    @GetMapping("/userinfo/base")
    public Result<?> selectBaseInfo() {
        return userInfoService.selectBaseInfo();
    }
}
