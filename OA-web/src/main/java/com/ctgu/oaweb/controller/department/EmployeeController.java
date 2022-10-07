package com.ctgu.oaweb.controller.department;

import com.ctgu.oacommon.vo.EmployeeConditionVO;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oadepartment.service.EmployeeManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zhang Jinming
 * @date 21/8/2022 下午3:46
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
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
