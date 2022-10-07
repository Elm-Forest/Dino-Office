package com.ctgu.oaweb.controller.checkingin;

import com.ctgu.oacheckingin.service.CheckService;
import com.ctgu.oacommon.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;


/**
 * @author Li Zihan
 */
@RestController
@RequestMapping(value = "/check")
@Api(tags = "考勤管理")
public class CheckController {

    @Autowired
    private CheckService checkService;

    @PostMapping("/sign_in")
    @ApiOperation(value = "用户签到")
    public Result<?> signIn() throws ParseException {
        return checkService.signIn();
    }

    @PostMapping("/sign_out")
    @ApiOperation(value = "用户签退")
    public Result<?> signOut() throws ParseException {
        return checkService.signOut();
    }

    @GetMapping("/statisticsById")
    @ApiOperation(value = "管理员根据userId查询考勤情况")
    public Result<?> findStatisticsById(@RequestParam("userId") Long userId) {
        return checkService.findStatisticsById(userId);
    }

    @GetMapping("/statistics")
    @ApiOperation(value = "管理员查询所有员工考勤情况")
    public Result<?> findStatistics() {
        return checkService.findStatistics();
    }

    @PutMapping("/set_attendance")
    @ApiOperation(value = "管理员设置上下班时间")
    public Result<?> setAttendanceRole(@RequestParam("startTime") Date startTime, @RequestParam("backTime") Date backTime) {
        return checkService.setAttendanceRole(startTime, backTime);
    }

    @PutMapping("/set_holiday")
    @ApiOperation(value = "管理员设置用户通用假期时间")
    public Result<?> setHoilday(@RequestParam("beginTime") Date beginTime, @RequestParam("endTime") Date endTime) {
        return checkService.setHoilday(beginTime, endTime);
    }

    @PutMapping("/set_holidayById")
    @ApiOperation(value = "管理员为指定用户授予放假权限")
    public Result<?> setHoildayById(@RequestParam("userId") Long userId) {
        return checkService.setHoildayById(userId);
    }

}
