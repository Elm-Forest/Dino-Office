package com.ctgu.web.controller.schedule;

import com.ctgu.common.models.dto.Result;
import com.ctgu.schedule.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Li Zihan
 */
@Api(tags = "用户日程管理")
@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {

    @Resource
    private ScheduleService scheduleService;

    @GetMapping
    @ApiOperation(value = "用户查看日程表")
    public Result<?> findSchedule() {
        return scheduleService.seeSchedule();
    }

    @PostMapping
    @ApiOperation(value = "用户添加日程表")
    public Result<?> addSchedule() {
        return scheduleService.addSchedule();
    }

    @PutMapping
    @ApiOperation(value = "用户编辑日程表")
    public Result<?> setSchedule(@RequestParam("scheduleTitle") String scheduleTitle, @RequestParam("scheduleContent") String scheduleContent, @RequestParam("beginTime") Date beginTime, @RequestParam("endTime") Date endTime) {
        return scheduleService.setSchedule(scheduleTitle, scheduleContent, beginTime, endTime);
    }

    @DeleteMapping
    @ApiOperation(value = "用户删除日程表")
    public Result<?> delSchedule() {
        return scheduleService.delSchedule();
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
    @ApiOperation(value = "用户查看联系人日程表")
    public Result<?> seeFriends(@RequestParam("friendId") Long friendId) {
        return scheduleService.seeFriends(friendId);
    }

}
