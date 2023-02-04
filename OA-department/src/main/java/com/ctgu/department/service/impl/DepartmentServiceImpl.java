package com.ctgu.department.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctgu.common.dao.department.DepartmentMapper;
import com.ctgu.common.dao.user.UserInfoMapper;
import com.ctgu.common.dao.user.UserMapper;
import com.ctgu.common.entity.Department;
import com.ctgu.common.entity.User;
import com.ctgu.common.entity.UserInfo;
import com.ctgu.common.exception.BizException;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.DepartmentVO;
import com.ctgu.common.utils.RandomHeadImg;
import com.ctgu.common.utils.ThreadHolder;
import com.ctgu.department.service.DepartmentService;
import com.ctgu.oss.service.UploadService;
import com.ctgu.user.service.core.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Objects;

import static com.ctgu.common.constants.CommonConst.STAFF_ONLINE;
import static com.ctgu.common.constants.FileConst.DEPARTMENT_FILE_PATH;
import static com.ctgu.common.constants.RoleConst.CEO;
import static com.ctgu.common.constants.RoleConst.SUPER_RIGHTS;

/**
 * @author 李嘉超
 * @author zhang jinming
 * @version 1.0
 * @date 2022/8/16 15:46
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private UploadService uploadService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private AuthService authService;

    @Override
    public Result<Department> selectDepartment() {
        User currentUser = ThreadHolder.getCurrentUser();
        UserInfo userInfo = userInfoMapper.selectById(currentUser.getId());
        Department department = departmentMapper.selectById(userInfo.getDeptId());
        return Result.ok(department);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> uploadHeadImage(MultipartFile headImg) {
        User currentUser = ThreadHolder.getCurrentUser();
        UserInfo userInfo = userInfoMapper.selectById(currentUser.getId());
        String filePath = DEPARTMENT_FILE_PATH;
        filePath = uploadService.uploadFile(headImg, filePath);
        int update = departmentMapper.update(Department.builder()
                .headImg(filePath)
                .build(), new LambdaQueryWrapper<Department>()
                .eq(Department::getId, userInfo.getDeptId()));
        if (update == 0) {
            throw new RuntimeException("上传失败！");
        }
        return Result.ok(filePath);
    }

    @Override
    public Result<?> selectHeadImg() {
        User currentUser = ThreadHolder.getCurrentUser();
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
        User currentUser = ThreadHolder.getCurrentUser();
        if (userInfoMapper.selectById(currentUser.getId()).getStatus() == STAFF_ONLINE) {
            throw new BizException("您已经处于入职状态！");
        }
        Department departmentBuilder = Department.builder()
                .address(departmentVo.getAddress())
                .headImg(departmentVo.getHeadImg())
                .name(departmentVo.getName())
                .phone(departmentVo.getPhone())
                .describe(departmentVo.getDesc())
                .build();
        int insert = departmentMapper.insert(departmentBuilder);
        if (insert == 0) {
            throw new RuntimeException("发生错误，请重试！");
        }
        int update = userInfoMapper.updateById(UserInfo.builder()
                .id(currentUser.getId())
                .status(STAFF_ONLINE)
                .deptId(departmentBuilder.getId())
                .build());
        if (update == 0) {
            throw new RuntimeException("发生错误，请重试！");
        }
        update = userMapper.updateById(User.builder()
                .id(currentUser.getId())
                .role(CEO)
                .rights(SUPER_RIGHTS)
                .build());
        if (update == 0) {
            throw new RuntimeException("发生错误，请重试！");
        }
        authService.logout();
        return Result.ok("创建成功，您的权限发生变化，您需要重新登陆！");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Boolean> updateDepartment(DepartmentVO departmentVO) {
        User currentUser = ThreadHolder.getCurrentUser();
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
            throw new RuntimeException("发生错误，请重试！");
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Boolean> deleteDepartment() {
        User currentUser = ThreadHolder.getCurrentUser();
        UserInfo userInfo = userInfoMapper.selectById(currentUser.getId());
        int delete = departmentMapper.deleteById(userInfo.getDeptId());
        if (delete == 0) {
            throw new RuntimeException("发生错误，请重试！");
        }
        return Result.ok();
    }

    @Override
    public Result<?> selectDepartmentByName(String name){
        return Result.ok(departmentMapper.selectList(new LambdaQueryWrapper<Department>()
                .like(Department::getName, name)));
    }
}
