package com.dino.department.service;

import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.EmployeeConditionVO;

/**
 * 雇员管理服务
 *
 * @author Zhang Jinming
 */
public interface EmployeeManagementService {
    /**
     * 根据条件查询雇员
     *
     * @param employeeConditionVO 查询条件
     * @return 雇员信息
     */
    Result<?> selectEmployeeFromDepartment(EmployeeConditionVO employeeConditionVO);

    /**
     * 根据条件查询雇员
     *
     * @param employeeConditionVO 查询条件
     * @return 雇员信息
     */
    Result<?> selectEmployeeFromRole(EmployeeConditionVO employeeConditionVO);

    /**
     * 解雇雇员
     *
     * @param id 雇员id
     * @return 是否解雇成功
     */
    Result<?> fireEmployee(Long id);

    /**
     * 入职雇员
     *
     * @param id   雇员id
     * @param role 雇员角色
     * @return 是否入职成功
     */
    Result<?> entryEmployee(Long id, Integer role);

    /**
     * 更新雇员信息
     *
     * @param id     雇员id
     * @param role   雇员角色
     * @param rights 雇员权限
     * @return 是否更新成功
     */
    Result<?> updateEmployeeInfo(Long id, Integer role, Integer rights);
}
