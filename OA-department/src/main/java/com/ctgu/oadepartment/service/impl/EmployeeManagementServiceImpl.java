package com.ctgu.oadepartment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.oacommon.entity.User;
import com.ctgu.oacommon.entity.UserInfo;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oacommon.vo.EmployeeConditionVO;
import com.ctgu.oacommon.vo.EmployeeVO;
import com.ctgu.oacommon.vo.PageResult;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oadepartment.mapper.EmployeeMapper;
import com.ctgu.oadepartment.service.EmployeeManagementService;
import com.ctgu.oauser.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ctgu.oacommon.constant.CommonConst.STAFF_OFFLINE;
import static com.ctgu.oacommon.constant.CommonConst.STAFF_ONLINE;
import static com.ctgu.oacommon.constant.RoleConst.*;

/**
 * @author Zhang Jinming
 * @date 21/8/2022 下午2:43
 */
@Service
public class EmployeeManagementServiceImpl implements EmployeeManagementService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<?> selectEmployeeFromDepartment(EmployeeConditionVO employeeConditionVO) {
        Long id = UserThreadHolder.getCurrentUser().getId();
        Long deptId = employeeMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        int currentPage = employeeConditionVO.getCurrent();
        int size = employeeConditionVO.getSize();
        Page<EmployeeVO> page = new Page<>(currentPage, size);
        System.out.println(employeeConditionVO);
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
        User currentUser = UserThreadHolder.getCurrentUser();
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
