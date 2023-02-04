package com.ctgu.web.controller.checking;

import com.ctgu.checking.service.core.CheckService;
import com.ctgu.common.models.dto.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;


/**
 * @author Li Zihan
 */
@Api(tags = "考勤管理")
@RestController
@RequestMapping(value = "/check")
public class CheckController {

    @Resource
    private CheckService checkService;

    @PostMapping("/in")
    @ApiOperation(value = "用户签到")
    public Result<?> signIn() throws ParseException {
        return checkService.signIn();
    }

    @PostMapping("/out")
    @ApiOperation(value = "用户签退")
    public Result<?> signOut() throws ParseException {
        return checkService.signOut();
    }

    @GetMapping("/admin/statistic")
    @ApiOperation(value = "管理员根据userId查询考勤情况")
    public Result<?> findStatisticsById(@RequestParam("userId") Long userId) {
        return checkService.findStatisticsById(userId);
    }

    @GetMapping("/admin/statistic/list")
    @ApiOperation(value = "管理员查询所有员工考勤情况")
    public Result<?> findStatistics() {
        return checkService.findStatistics();
    }

    @PutMapping("/admin/attendance")
    @ApiOperation(value = "管理员设置上下班时间")
    public Result<?> setAttendanceRole(@RequestParam("startTime") Date startTime, @RequestParam("backTime") Date backTime) {
        return checkService.setAttendanceRole(startTime, backTime);
    }

    @PutMapping("/admin/holiday")
    @ApiOperation(value = "管理员设置用户通用假期时间")
    public Result<?> setHoliday(@RequestParam("beginTime") Date beginTime, @RequestParam("endTime") Date endTime) {
        return checkService.setHoliday(beginTime, endTime);
    }

    @PutMapping("/admin/holiday/user")
    @ApiOperation(value = "管理员为指定用户授予放假权限")
    public Result<?> setHolidayById(@RequestParam("userId") Long userId) {
        return checkService.setHolidayById(userId);
    }

}
