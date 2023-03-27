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
@Api(tags = "部门日程管理")
@RestController
@RequestMapping(value = "/schedule")
public class ScheduleDeptController {

    @Resource
    private ScheduleService scheduleService;

    @PostMapping("/dept")
    @ApiOperation(value = "添加部门日程表")
    public Result<?> addScheduleDept(@RequestParam("scheduleTitle") String scheduleTitle,
                                     @RequestParam("scheduleContent") String scheduleContent,
                                     @RequestParam("beginTime") String beginTime,
                                     @RequestParam("endTime") String endTime) {
        return scheduleService.addScheduleDept(scheduleTitle, scheduleContent, beginTime, endTime);
    }

    @DeleteMapping("/dept")
    @ApiOperation(value = "删除部门日程表")
    public Result<?> delScheduleDept(@RequestParam("scheduleDeptId") Long scheduleDeptId) {
        return scheduleService.delScheduleDept(scheduleDeptId);
    }

    @PutMapping("/dept")
    @ApiOperation(value = "编辑部门日程表")
    public Result<?> updateScheduleDept(@RequestParam("scheduleDeptId") Long scheduleDeptId,
                                        @RequestParam("scheduleTitle") String scheduleTitle,
                                        @RequestParam("scheduleContent") String scheduleContent,
                                        @RequestParam("beginTime") String beginTime,
                                        @RequestParam("endTime") String endTime) {
        return scheduleService.updateScheduleDept(scheduleDeptId, scheduleTitle, scheduleContent, beginTime, endTime);
    }

    @GetMapping("/dept")
    @ApiOperation(value = "查看所有部门日程表")
    public Result<?> findScheduleDept() {
        return scheduleService.findScheduleDept();
    }

}
