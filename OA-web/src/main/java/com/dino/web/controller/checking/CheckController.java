package com.dino.web.controller.checking;

import com.dino.checking.service.core.CheckService;
import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * @author Li Zihan
 */
@Api(tags = "考勤管理")
@RestController
@RequestMapping(value = "/check")
public class CheckController {

    @Resource
    private CheckService checkService;

    @GetMapping
    @ApiOperation(value = "员工查询考勤日志表")
    public Result<?> getUserCheckListById() {
        return checkService.getCheckListById();
    }

    @GetMapping("/absent")
    @ApiOperation(value = "员工查询缺勤日志表")
    public Result<?> getAbsent() {
        return checkService.getAbsent();
    }

    @GetMapping("/admin")
    @ApiOperation(value = "管理员查询考勤日志表")
    public Result<?> getUserCheckList() {
        return checkService.getCheckList();
    }

    @PostMapping("/sign")
    @ApiOperation(value = "员工签到/签退")
    public Result<?> sign(@RequestParam("signType") Integer type) throws ParseException {
        return checkService.sign(type);
    }

    @GetMapping("/apply")
    @ApiOperation(value = "员工申请补卡/请假")
    public Result<?> apply(ApplyVO applyVO) throws ParseException {
        return checkService.apply(applyVO);
    }

    @GetMapping("/test")
    @ApiOperation(value = "测试缺勤功能")
    public void testAbsence() throws ParseException {
        checkService.absent();
    }

    @GetMapping("/judge")
    @ApiOperation(value = "判断是签到还是签退")
    public Result<?> judge() throws ParseException {
        return checkService.judge();
    }

    @PostMapping("/admin/setType")
    @ApiOperation(value = "设置考勤类型")
    public Result<?> setCheck(CheckVO checkVO) throws ParseException {
        return checkService.setCheck(checkVO);
    }

    @PostMapping("/admin/deleteCheck")
    @ApiOperation(value = "删除考勤记录")
    public Result<?> deleteCheck(@RequestParam("id") Long id) throws ParseException {
        return checkService.deleteCheck(id);
    }

    @PostMapping("/admin/getCheck")
    @ApiOperation(value = "管理员根据条件查询考勤记录")
    public Result<?> selectCheck(CheckConditionVO checkConditionVO) throws ParseException {
        return checkService.selectCheck(checkConditionVO);
    }

    @DeleteMapping("/admin/rule")
    @ApiOperation(value = "管理员删除考勤规则")
    public Result<?> delRule(@RequestParam("id") Long id) {
        return checkService.delRule(id);
    }

    @PutMapping("admin/rule")
    @ApiOperation(value = "管理员修改考勤规则")
    public Result<?> setRule(CheckRuleVO checkRuleVO) {
        return checkService.setRule(checkRuleVO);
    }

    @GetMapping("admin/rule")
    @ApiOperation(value = "管理员查看考勤规则")
    public Result<?> findRule() {
        return checkService.findRule();
    }

    @PostMapping("/admin/rule")
    @ApiOperation(value = "管理员设置考勤规则")
    public Result<?> addRule(CheckRuleVO checkRuleVO) throws ParseException {
        return checkService.addRule(checkRuleVO);
    }

    @GetMapping("admin/apply")
    @ApiOperation(value = "管理员查看补卡申请")
    public Result<?> findApply() {
        return checkService.findApply();
    }

    @DeleteMapping("admin/apply/{id}")
    @ApiOperation(value = "管理员拒绝补卡申请")
    public Result<?> deleteApply(@PathVariable Long id) {
        return checkService.deleteApply(id);
    }

    @PutMapping("admin/apply/{id}")
    @ApiOperation(value = "管理员同意补卡申请")
    public Result<?> approveApply(@PathVariable Long id) {
        return checkService.approveApply(id);
    }

}
