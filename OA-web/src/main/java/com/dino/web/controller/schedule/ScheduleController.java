package com.dino.web.controller.schedule;

import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.ScheduleArrangeVO;
import com.dino.schedule.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Li Zihan
 */
@Api(tags = "个人日程管理")
@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {

    @Resource
    private ScheduleService scheduleService;

    @PostMapping
    @ApiOperation(value = "用户添加日程表")
    public Result<?> addSchedule(ScheduleArrangeVO scheduleArrangeVO) {
        return scheduleService.addSchedule(scheduleArrangeVO);
    }

    @DeleteMapping
    @ApiOperation(value = "用户删除日程表")
    public Result<?> delSchedule(@RequestParam("scheduleId") Long scheduleId) {
        return scheduleService.delSchedule(scheduleId);
    }

    @PutMapping
    @ApiOperation(value = "用户编辑日程表")
    public Result<?> setSchedule(ScheduleArrangeVO scheduleArrangeVO) {
        return scheduleService.setSchedule( scheduleArrangeVO);
    }

    @GetMapping("/list")
    @ApiOperation(value = "用户查看所有日程表")
    public Result<?> findSchedule() {
        return scheduleService.findSchedule();
    }

    @GetMapping
    @ApiOperation(value = "用户查看指定日程表")
    public Result<?> findScheduleById(@RequestParam("scheduleId") Long scheduleId) {
        return scheduleService.findScheduleById(scheduleId);
    }

    @PostMapping("/friend")
    @ApiOperation(value = "用户添加联系人")
    public Result<?> addFriends(@RequestParam("friendId") Long friendId) {
        return scheduleService.addFriends(friendId);
    }

    @DeleteMapping("/friend")
    @ApiOperation(value = "用户删除联系人")
    public Result<?> delFriends(@RequestParam("friendId") Long friendId) {
        return scheduleService.delFriends(friendId);
    }

    @GetMapping("/friend")
    @ApiOperation(value = "用户查看所有联系人")
    public Result<?> seeFriends() {
        return scheduleService.seeFriends();
    }

    @GetMapping("/friendSchedule")
    @ApiOperation(value = "用户查看指定联系人日程表")
    public Result<?> seeFriendsSchedule(@RequestParam("friendId") Long friendId) {
        return scheduleService.seeFriendsSchedule(friendId);
    }

}
