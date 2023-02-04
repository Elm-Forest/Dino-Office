package com.ctgu.department.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.common.dao.user.UserInfoMapper;
import com.ctgu.common.dao.user.UserMapper;
import com.ctgu.common.entity.User;
import com.ctgu.common.entity.UserInfo;
import com.ctgu.common.models.dto.PageResult;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.EmployeeConditionVO;
import com.ctgu.common.models.vo.EmployeeVO;
import com.ctgu.common.utils.ThreadHolder;
import com.ctgu.department.service.EmployeeManagementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.ctgu.common.constants.CommonConst.STAFF_OFFLINE;
import static com.ctgu.common.constants.CommonConst.STAFF_ONLINE;
import static com.ctgu.common.constants.RoleConst.*;

/**
 * @author Zhang Jinming
 * @date 21/8/2022 下午2:43
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
        Page<EmployeeVO> page = new Page<>(currentPage, size);
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
        Page<EmployeeVO> page = new Page<>(currentPage, size);
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
