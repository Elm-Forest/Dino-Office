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
@Api(tags = "部门日程管理")
@RestController
@RequestMapping(value = "/schedule")
public class ScheduleDeptController {

    @Resource
    private ScheduleService scheduleService;

    @PostMapping("/dept")
    @ApiOperation(value = "添加部门日程表")
    public Result<?> addScheduleDept(@RequestParam("deptId") Long deptId) {
        return scheduleService.addScheduleDept(deptId);
    }

    @DeleteMapping("/dept")
    @ApiOperation(value = "删除部门日程表")
    public Result<?> delScheduleDept(@RequestParam("deptId") Long deptId) {
        return scheduleService.delScheduleDept(deptId);
    }

    @PutMapping("/dept")
    @ApiOperation(value = "编辑部门日程表")
    public Result<?> updateScheduleDept(@RequestParam("deptId") Long deptId, @RequestParam("scheduleTitle") String scheduleTitle, @RequestParam("scheduleContent") String scheduleContent, @RequestParam("beginTime") Date beginTime, @RequestParam("endTime") Date endTime) {
        return scheduleService.updateScheduleDept(deptId, scheduleTitle, scheduleContent, beginTime, endTime);
    }

    @GetMapping("/dept")
    @ApiOperation(value = "查看部门日程表")
    public Result<?> findScheduleDept(@RequestParam("deptId") Long deptId) {
        return scheduleService.findScheduleDept(deptId);
    }

}
