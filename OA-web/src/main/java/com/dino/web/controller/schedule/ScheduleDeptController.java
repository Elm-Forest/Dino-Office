package com.dino.web.controller.schedule;

import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.ScheduleDepartmentVO;
import com.dino.schedule.service.ScheduleService;
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
    public Result<?> addScheduleDept(ScheduleDepartmentVO scheduleDepartmentVO) {
        return scheduleService.addScheduleDept(scheduleDepartmentVO);
    }

    @DeleteMapping("/dept")
    @ApiOperation(value = "删除部门日程表")
    public Result<?> delScheduleDept(@RequestParam("scheduleDeptId") Long scheduleDeptId) {
        return scheduleService.delScheduleDept(scheduleDeptId);
    }

    @PutMapping("/dept")
    @ApiOperation(value = "编辑部门日程表")
    public Result<?> updateScheduleDept(ScheduleDepartmentVO scheduleDepartmentVO) {
        return scheduleService.updateScheduleDept(scheduleDepartmentVO);
    }

    @GetMapping("/dept")
    @ApiOperation(value = "查看部门日程表")
    public Result<?> findScheduleDept() {
        return scheduleService.findScheduleDept();
    }

}
