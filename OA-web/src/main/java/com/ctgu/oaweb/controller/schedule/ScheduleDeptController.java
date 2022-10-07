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
@Api(tags = "部门日程管理")
public class ScheduleDeptController {

    @Autowired
    private ScheduleService scheduleService;

    @PutMapping("/add_dept")
    @ApiOperation(value = "添加部门日程表")
    public Result<?> addScheduleDept(@RequestParam("deptId") Long deptId) {
        return scheduleService.addScheduleDept(deptId);
    }

    @DeleteMapping("/del_dept")
    @ApiOperation(value = "删除部门日程表")
    public Result<?> delScheduleDept(@RequestParam("deptId") Long deptId) {
        return scheduleService.delScheduleDept(deptId);
    }

    @PutMapping("/update_dept")
    @ApiOperation(value = "编辑部门日程表")
    public Result<?> updateScheduleDept(@RequestParam("deptId") Long deptId, @RequestParam("scheduleTitle") String scheduleTitle, @RequestParam("scheduleContent") String scheduleContent, @RequestParam("beginTime") Date beginTime, @RequestParam("endTime") Date endTime) {
        return scheduleService.updateScheduleDept(deptId, scheduleTitle, scheduleContent, beginTime, endTime);
    }

    @GetMapping("/find_dept")
    @ApiOperation(value = "查看部门日程表")
    public Result<?> findScheduleDept(@RequestParam("deptId") Long deptId) {
        return scheduleService.findScheduleDept(deptId);
    }

}
