package com.dino.web.controller.department;

import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.EmployeeConditionVO;
import com.dino.department.service.EmployeeManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Zhang Jinming
 */
@Api(tags = "雇员管理")
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private EmployeeManagementService employeeManagementService;

    @ApiOperation(value = "根据部门查询雇员")
    @GetMapping("/list/all")
    public Result<?> selectEmployeeFromDepartment(EmployeeConditionVO employeeConditionVO) {
        return employeeManagementService.selectEmployeeFromDepartment(employeeConditionVO);
    }

    @ApiOperation(value = "根据角色查询雇员")
    @GetMapping("/list/role")
    public Result<?> selectEmployeeFromRole(EmployeeConditionVO employeeConditionVO) {
        return employeeManagementService.selectEmployeeFromRole(employeeConditionVO);
    }

    @ApiOperation(value = "根据权限查询雇员")
    @PostMapping("/fire")
    public Result<?> fireEmployee(@RequestParam("id") Long id) {
        return employeeManagementService.fireEmployee(id);
    }

    @ApiOperation(value = "雇员入职")
    @PostMapping("/entry")
    public Result<?> entryEmployee(@RequestParam("id") Long id,
                                   @RequestParam("role") Integer role) {
        return employeeManagementService.entryEmployee(id, role);
    }

    @ApiOperation(value = "雇员信息修改")
    @PutMapping
    public Result<?> updateEmployeeInfo(@RequestParam("id") Long id,
                                        @RequestParam("role") Integer role,
                                        @RequestParam("rights") Integer rights) {
        return employeeManagementService.updateEmployeeInfo(id, role, rights);
    }
}
