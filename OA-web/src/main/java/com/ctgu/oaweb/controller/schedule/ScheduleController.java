package com.ctgu.oaweb.controller.schedule;

import com.ctgu.oacommon.vo.Result;
import com.ctgu.oaschedule.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author Li Zihan
 */
@RestController
@RequestMapping(value = "/schedule")
@Api(tags = "用户日程管理")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/see")
    @ApiOperation(value = "用户查看日程表")
    public Result<?> findSchedule() {
        return scheduleService.seeSchedule();
    }

    @PutMapping("/add")
    @ApiOperation(value = "用户添加日程表")
    public Result<?> addSchedule() {
        return scheduleService.addSchedule();
    }

    @PutMapping("/set")
    @ApiOperation(value = "用户编辑日程表")
    public Result<?> setSchedule(@RequestParam("scheduleTitle") String scheduleTitle, @RequestParam("scheduleContent") String scheduleContent, @RequestParam("beginTime") Date beginTime, @RequestParam("endTime") Date endTime) {
        return scheduleService.setSchedule(scheduleTitle, scheduleContent, beginTime, endTime);
    }

    @DeleteMapping("/del")
    @ApiOperation(value = "用户删除日程表")
    public Result<?> delSchedule() {
        return scheduleService.delSchedule();
    }

    @PostMapping("/add_friends")
    @ApiOperation(value = "用户添加联系人")
    public Result<?> addFriends(@RequestParam("friendId") Long friendId) {
        return scheduleService.addFriends(friendId);
    }

    @DeleteMapping("/del_friends")
    @ApiOperation(value = "用户删除联系人")
    public Result<?> delFriends(@RequestParam("friendId") Long friendId) {
        return scheduleService.delFriends(friendId);
    }

    @GetMapping("/see_friends")
    @ApiOperation(value = "用户查看联系人日程表")
    public Result<?> seeFriends(@RequestParam("friendId") Long friendId) {
        return scheduleService.seeFriends(friendId);
    }

}
