package com.ctgu.department.service;

import com.ctgu.common.models.vo.EmployeeConditionVO;
import com.ctgu.common.models.dto.Result;

/**
 * @author Zhang Jinming
 * @date 21/8/2022 下午2:43
 */
public interface EmployeeManagementService {
    Result<?> selectEmployeeFromDepartment(EmployeeConditionVO employeeConditionVO);

    Result<?> selectEmployeeFromRole(EmployeeConditionVO employeeConditionVO);

    Result<?> fireEmployee(Long id);

    Result<?> entryEmployee(Long id, Integer role);

    Result<?> updateEmployeeInfo(Long id, Integer role, Integer rights);
}
