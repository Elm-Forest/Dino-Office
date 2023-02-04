package com.ctgu.web.controller.department;

import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.EmployeeConditionVO;
import com.ctgu.department.service.EmployeeManagementService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Zhang Jinming
 * @date 21/8/2022 下午3:46
 */
@Api(tags = "雇员管理")
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private EmployeeManagementService employeeManagementService;

    @GetMapping("/list/all")
    public Result<?> selectEmployeeFromDepartment(EmployeeConditionVO employeeConditionVO) {
        return employeeManagementService.selectEmployeeFromDepartment(employeeConditionVO);
    }

    @GetMapping("/list/role")
    public Result<?> selectEmployeeFromRole(EmployeeConditionVO employeeConditionVO) {
        return employeeManagementService.selectEmployeeFromRole(employeeConditionVO);
    }

    @PostMapping("/fire")
    public Result<?> fireEmployee(@RequestParam("id") Long id) {
        return employeeManagementService.fireEmployee(id);
    }

    @PostMapping("/entry")
    public Result<?> entryEmployee(@RequestParam("id") Long id,
                                   @RequestParam("role") Integer role) {
        return employeeManagementService.entryEmployee(id, role);
    }

    @PutMapping
    public Result<?> updateEmployeeInfo(@RequestParam("id") Long id,
                                        @RequestParam("role") Integer role,
                                        @RequestParam("rights") Integer rights) {
        return employeeManagementService.updateEmployeeInfo(id, role, rights);
    }
}
