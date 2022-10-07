package com.ctgu.oadepartment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctgu.oacommon.entity.Department;
import com.ctgu.oacommon.entity.User;
import com.ctgu.oacommon.entity.UserInfo;
import com.ctgu.oacommon.exception.BizException;
import com.ctgu.oacommon.utils.RandomHeadImg;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oacommon.vo.DepartmentVO;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oadepartment.mapper.DepartmentMapper;
import com.ctgu.oadepartment.service.DepartmentService;
import com.ctgu.oaoss.service.UploadService;
import com.ctgu.oauser.mapper.UserInfoMapper;
import com.ctgu.oauser.mapper.UserMapper;
import com.ctgu.oauser.service.core.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.ctgu.oacommon.constant.CommonConst.STAFF_ONLINE;
import static com.ctgu.oacommon.constant.FileConst.DEPARTMENT_FILE_PATH;
import static com.ctgu.oacommon.constant.RoleConst.CEO;
import static com.ctgu.oacommon.constant.RoleConst.SUPER_RIGHTS;

/**
 * @author 李嘉超
 * @author zhang jinming
 * @version 1.0
 * @date 2022/8/16 15:46
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthService authService;

    @Override
    public Result<Department> selectDepartment() {
        User currentUser = UserThreadHolder.getCurrentUser();
        UserInfo userInfo = userInfoMapper.selectById(currentUser.getId());
        Department department = departmentMapper.selectById(userInfo.getDeptId());
        return Result.ok(department);
    }

    @Override
    public Result<?> uploadHeadImage(MultipartFile headImg) {
        User currentUser = UserThreadHolder.getCurrentUser();
        UserInfo userInfo = userInfoMapper.selectById(currentUser.getId());
        String filePath = DEPARTMENT_FILE_PATH;
        filePath = uploadService.uploadFile(headImg, filePath);
        int update;
        try {
            update = departmentMapper.update(Department.builder()
                    .headImg(filePath)
                    .build(), new LambdaQueryWrapper<Department>()
                    .eq(Department::getId, userInfo.getDeptId()));
            if (update == 0) {
                throw new BizException("上传失败！");
            }
        } catch (BizException e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok(filePath);
    }

    @Override
    public Result<?> selectHeadImg() {
        User currentUser = UserThreadHolder.getCurrentUser();
        UserInfo userInfo = userInfoMapper.selectById(currentUser.getId());
        Department department = departmentMapper.selectOne(new LambdaQueryWrapper<Department>()
                .select(Department::getHeadImg)
                .eq(Department::getId, userInfo.getDeptId()));
        String url;
        if (department.getHeadImg() != null && !Objects.equals(department.getHeadImg(), "")) {
            url = department.getHeadImg();
        } else {
            url = RandomHeadImg.randomHeadImg();
            departmentMapper.updateById(Department.builder()
                    .id(userInfo.getDeptId())
                    .headImg(url)
                    .build());
        }
        return Result.ok(url);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> insertDepartment(DepartmentVO departmentVo) {
        User currentUser = UserThreadHolder.getCurrentUser();
        if (userInfoMapper.selectById(currentUser.getId()).getStatus() == STAFF_ONLINE) {
            return Result.fail("您已经处于入职状态！");
        }
        try {
            Department departmentBuilder = Department.builder()
                    .address(departmentVo.getAddress())
                    .headImg(departmentVo.getHeadImg())
                    .name(departmentVo.getName())
                    .phone(departmentVo.getPhone())
                    .describe(departmentVo.getDesc())
                    .build();
            int insert = departmentMapper.insert(departmentBuilder);
            if (insert == 0) {
                throw new BizException("发生错误，请重试！");
            }
            int update = userInfoMapper.updateById(UserInfo.builder()
                    .id(currentUser.getId())
                    .status(STAFF_ONLINE)
                    .deptId(departmentBuilder.getId())
                    .build());
            if (update == 0) {
                throw new BizException("发生错误，请重试！");
            }
            update = userMapper.updateById(User.builder()
                    .id(currentUser.getId())
                    .role(CEO)
                    .rights(SUPER_RIGHTS)
                    .build());
            if (update == 0) {
                throw new BizException("发生错误，请重试！");
            }
            authService.logout();
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok("创建成功，您的权限发生变化，您需要重新登陆！");
    }

    @Override
    public Result<Boolean> updateDepartment(DepartmentVO departmentVO) {
        User currentUser = UserThreadHolder.getCurrentUser();
        UserInfo userInfo = userInfoMapper.selectById(currentUser.getId());
        Department departmentBuilder = Department.builder()
                .id(userInfo.getDeptId())
                .address(departmentVO.getAddress())
                .name(departmentVO.getName())
                .phone(departmentVO.getPhone())
                .describe(departmentVO.getDesc())
                .build();
        int update = departmentMapper.updateById(departmentBuilder);
        if (update == 0) {
            return Result.fail();
        }
        return Result.ok();
    }

    @Override
    public Result<Boolean> deleteDepartment() {
        User currentUser = UserThreadHolder.getCurrentUser();
        UserInfo userInfo = userInfoMapper.selectById(currentUser.getId());
        int delete = departmentMapper.deleteById(userInfo.getDeptId());
        if (delete == 0) {
            return Result.fail();
        }
        return Result.ok();
    }
}
