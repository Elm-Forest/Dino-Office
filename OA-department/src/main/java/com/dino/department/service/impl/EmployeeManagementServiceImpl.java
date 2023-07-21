package com.dino.department.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dino.common.dao.user.UserInfoMapper;
import com.dino.common.dao.user.UserMapper;
import com.dino.common.entity.User;
import com.dino.common.entity.UserInfo;
import com.dino.common.models.dto.EmployeeDTO;
import com.dino.common.models.dto.PageResult;
import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.EmployeeConditionVO;
import com.dino.common.utils.ThreadHolder;
import com.dino.department.service.EmployeeManagementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.dino.common.constants.CommonConst.STAFF_OFFLINE;
import static com.dino.common.constants.CommonConst.STAFF_ONLINE;
import static com.dino.common.constants.RoleConst.*;

/**
 * @author Zhang Jinming
 */
@Service
public class EmployeeManagementServiceImpl implements EmployeeManagementService {
    @Resource
    private UserInfoMapper employeeMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Result<?> selectEmployeeFromDepartment(EmployeeConditionVO employeeConditionVO) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = employeeMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        int currentPage = employeeConditionVO.getCurrent();
        int size = employeeConditionVO.getSize();
        Page<EmployeeDTO> page = new Page<>(currentPage, size);
        employeeMapper.selectEmployee(page,
                deptId,
                employeeConditionVO.getStatus(),
                employeeConditionVO.getName(),
                employeeConditionVO.getRole(),
                employeeConditionVO.getRights());
        PageResult<?> employeePage = new PageResult<>(page.getRecords(), (int) page.getTotal());
        return Result.ok(employeePage);
    }

    @Override
    public Result<?> selectEmployeeFromRole(EmployeeConditionVO employeeConditionVO) {
        User currentUser = ThreadHolder.getCurrentUser();
        Long id = currentUser.getId();
        Long deptId = employeeMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        int currentPage = employeeConditionVO.getCurrent();
        int size = employeeConditionVO.getSize();
        Page<EmployeeDTO> page = new Page<>(currentPage, size);
        employeeMapper.selectEmployee(page,
                deptId,
                employeeConditionVO.getStatus(),
                employeeConditionVO.getName(),
                currentUser.getRole(),
                employeeConditionVO.getRights());
        PageResult<?> employeePage = new PageResult<>(page.getRecords(), (int) page.getTotal());
        return Result.ok(employeePage);
    }

    @Override
    public Result<?> fireEmployee(Long id) {
        employeeMapper.updateById(UserInfo.builder()
                .id(id)
                .status(STAFF_OFFLINE)
                .build());
        userMapper.updateById(User.builder()
                .id(id)
                .role(UN_EMPLOYED)
                .rights(FRESHMAN_RIGHTS)
                .build());
        return Result.ok();
    }

    @Override
    public Result<?> entryEmployee(Long id, Integer role) {
        employeeMapper.updateById(UserInfo.builder()
                .id(id)
                .status(STAFF_ONLINE)
                .build());
        userMapper.updateById(User.builder()
                .id(id)
                .role(role)
                .rights(USER_RIGHTS)
                .build());
        return Result.ok();
    }

    @Override
    public Result<?> updateEmployeeInfo(Long id, Integer role, Integer rights) {
        userMapper.updateById(User.builder()
                .id(id)
                .role(role)
                .rights(rights)
                .build());
        return Result.ok();
    }
}
