package com.dino.user.service.core.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dino.common.dao.department.DepartmentMapper;
import com.dino.common.dao.user.UserInfoMapper;
import com.dino.common.dao.user.UserMapper;
import com.dino.common.entity.Department;
import com.dino.common.entity.User;
import com.dino.common.entity.UserInfo;
import com.dino.common.exception.BizException;
import com.dino.common.models.dto.BaseInfoDTO;
import com.dino.common.models.dto.EntryInfoDTO;
import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.UserInfoVO;
import com.dino.common.utils.RandomHeadImg;
import com.dino.common.utils.ThreadHolder;
import com.dino.oss.service.UploadService;
import com.dino.user.service.common.UserCommonService;
import com.dino.user.service.core.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Objects;

import static com.dino.common.constants.CommonConst.STAFF_ONLINE;
import static com.dino.common.constants.FileConst.USER_FILE_PATH;

/**
 * @author Zhang Jinming
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserCommonService userCommonService;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private UploadService uploadService;

    @Override
    public Result<?> selectUserInfo() {
        UserInfo userInfo = userInfoMapper.selectById(ThreadHolder.getCurrentUser().getId());
        return Result.ok(userInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> updateUserInfo(UserInfoVO user) {
        user.setId(ThreadHolder.getCurrentUser().getId());
        int update = userCommonService.updateUserInfoById(user);
        if (update <= 0) {
            throw new RuntimeException("更新失败，请联系技术部门");
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> insertUserInfo(UserInfoVO user) {
        user.setId(ThreadHolder.getCurrentUser().getId());
        int update = userCommonService.insertUserInfoById(user);
        if (update <= 0) {
            throw new RuntimeException("更新失败，请联系技术部门");
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> updateUserHeadImg(MultipartFile headImg) {
        Long userId = ThreadHolder.getCurrentUser().getId();
        String filePath = USER_FILE_PATH;
        filePath = uploadService.uploadFile(headImg, filePath);
        int update = userInfoMapper.update(UserInfo.builder()
                .headImg(filePath)
                .build(), new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, userId));
        if (update == 0) {
            throw new RuntimeException("上传失败！");
        }
        return Result.ok(filePath);
    }

    @Override
    public Result<?> selectUserHeadImg() {
        Long userId = ThreadHolder.getCurrentUser().getId();
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, userId));
        String url;
        if (userInfo.getHeadImg() != null && !Objects.equals(userInfo.getHeadImg(), "")) {
            url = userInfo.getHeadImg();
        } else {
            url = RandomHeadImg.randomHeadImg();
            userInfoMapper.updateById(UserInfo.builder()
                    .id(userId)
                    .headImg(url)
                    .build());
        }
        return Result.ok(url);
    }

    @Override
    public Result<?> entryApply(Long deptId) {
        Long userId = ThreadHolder.getCurrentUser().getId();
        if (userInfoMapper.selectById(userId).getStatus() == STAFF_ONLINE) {
            throw new BizException("投递失败，您已处于在职状态!");
        }
        userInfoMapper.updateById(UserInfo.builder()
                .id(userId)
                .deptId(deptId)
                .build());
        return Result.ok();
    }

    @Override
    public Result<?> checkEntry() {
        UserInfo userInfo = userInfoMapper.selectById(ThreadHolder.getCurrentUser().getId());
        User user = userMapper.selectById(ThreadHolder.getCurrentUser().getId());
        return Result.ok(EntryInfoDTO.builder()
                .role(user.getRole())
                .rights(user.getRights())
                .status(userInfo.getStatus())
                .build());
    }

    @Override
    public Result<?> selectBaseInfo() {
        UserInfo userInfo = userInfoMapper.selectById(ThreadHolder.getCurrentUser().getId());
        User user = userMapper.selectById(userInfo.getId());
        Department department = departmentMapper.selectOne(new LambdaQueryWrapper<Department>()
                .eq(Department::getId, userInfo.getDeptId()));
        BaseInfoDTO baseInfoDTO = BaseInfoDTO.builder()
                .userName(userInfo.getName())
                .role(user.getRole())
                .deptName(department.getName())
                .deptUrl(department.getHeadImg())
                .build();
        return Result.ok(baseInfoDTO);
    }
}
