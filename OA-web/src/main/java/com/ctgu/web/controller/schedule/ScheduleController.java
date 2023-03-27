package com.ctgu.web.controller.schedule;

import com.ctgu.common.models.dto.Result;
import com.ctgu.schedule.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Li Zihan
 */
@Api(tags = "用户日程管理")
@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {

    @Resource
    private ScheduleService scheduleService;

    @GetMapping("/list")
    @ApiOperation(value = "用户查看所有日程表")
    public Result<?> findSchedule() {
        return scheduleService.seeSchedule();
    }

    @GetMapping("/one")
    @ApiOperation(value = "用户查看指定日程表")
    public Result<?> findScheduleById(@RequestParam("scheduleId") Long scheduleId) {
        return scheduleService.seeScheduleById(scheduleId);
    }

    @PostMapping
    @ApiOperation(value = "用户添加日程表")
    public Result<?> addSchedule(@RequestParam("scheduleTitle") String scheduleTitle,
                                 @RequestParam("scheduleContent") String scheduleContent,
                                 @RequestParam("beginTime") String beginTime,
                                 @RequestParam("endTime") String endTime) {
        return scheduleService.addSchedule(scheduleTitle, scheduleContent, beginTime, endTime);
    }

    @PutMapping
    @ApiOperation(value = "用户编辑日程表")
    public Result<?> setSchedule(@RequestParam("scheduleId") Long scheduleId,
                                 @RequestParam("scheduleTitle") String scheduleTitle,
                                 @RequestParam("scheduleContent") String scheduleContent,
                                 @RequestParam("beginTime") String beginTime,
                                 @RequestParam("endTime") String endTime) {
        return scheduleService.setSchedule(scheduleId, scheduleTitle, scheduleContent, beginTime, endTime);
    }

    @DeleteMapping
    @ApiOperation(value = "用户删除日程表")
    public Result<?> delSchedule(@RequestParam("scheduleId") Long scheduleId) {
        return scheduleService.delSchedule(scheduleId);
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
