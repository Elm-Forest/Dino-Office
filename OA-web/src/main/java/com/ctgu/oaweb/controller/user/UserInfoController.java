package com.ctgu.oaweb.controller.user;

import com.ctgu.oacommon.vo.Result;
import com.ctgu.oacommon.vo.UserInfoVO;
import com.ctgu.oauser.service.common.UserCommonService;
import com.ctgu.oauser.service.core.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zhang Jinming
 * @date 17/8/2022 上午11:18
 */
@RestController
@RequestMapping(value = "/user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserCommonService userCommonService;

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
        return Result.ok(userCommonService.selectDepartmentByName(name));
    }

    @GetMapping("/entry")
    public Result<?> checkEntry() {
        return userInfoService.checkEntry();
    }
}
