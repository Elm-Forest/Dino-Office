package com.ctgu.oauser.service.core.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctgu.oacommon.entity.User;
import com.ctgu.oacommon.entity.UserInfo;
import com.ctgu.oacommon.exception.BizException;
import com.ctgu.oacommon.utils.RandomHeadImg;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oacommon.vo.UserInfoVO;
import com.ctgu.oaoss.service.UploadService;
import com.ctgu.oauser.mapper.UserInfoMapper;
import com.ctgu.oauser.mapper.UserMapper;
import com.ctgu.oauser.service.common.UserCommonService;
import com.ctgu.oauser.service.core.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Objects;

import static com.ctgu.oacommon.constant.CommonConst.STAFF_ONLINE;
import static com.ctgu.oacommon.constant.FileConst.USER_FILE_PATH;

/**
 * @author Zhang Jinming
 * @date 16/8/2022 下午3:34
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserCommonService userCommonService;

    @Autowired
    private UploadService uploadService;

    @Override
    public Result<?> selectUserInfo() {
        UserInfo userInfo = userInfoMapper.selectById(UserThreadHolder.getCurrentUser().getId());
        return Result.ok(userInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> updateUserInfo(UserInfoVO user) {
        try {
            user.setId(UserThreadHolder.getCurrentUser().getId());
            int update = userCommonService.updateUserInfoById(user);
            if (update <= 0) {
                throw new BizException("更新失败，请联系技术部门");
            }
        } catch (Exception e) {
            Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> insertUserInfo(UserInfoVO user) {
        try {
            user.setId(UserThreadHolder.getCurrentUser().getId());
            int update = userCommonService.insertUserInfoById(user);
            if (update <= 0) {
                throw new BizException("更新失败，请联系技术部门");
            }
        } catch (Exception e) {
            Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> updateUserHeadImg(MultipartFile headImg) {
        Long userId = UserThreadHolder.getCurrentUser().getId();
        String filePath = USER_FILE_PATH;
        filePath = uploadService.uploadFile(headImg, filePath);
        int update;
        try {
            update = userInfoMapper.update(UserInfo.builder()
                    .headImg(filePath)
                    .build(), new LambdaQueryWrapper<UserInfo>()
                    .eq(UserInfo::getId, userId));
            if (update == 0) {
                throw new BizException("上传失败！");
            }
        } catch (BizException e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok(filePath);
    }

    @Override
    public Result<?> selectUserHeadImg() {
        Long userId = UserThreadHolder.getCurrentUser().getId();
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
        Long userId = UserThreadHolder.getCurrentUser().getId();
        if (userInfoMapper.selectById(userId).getStatus() == STAFF_ONLINE) {
            return Result.fail("投递失败，您已处于在职状态!");
        }
        userInfoMapper.updateById(UserInfo.builder()
                .id(userId)
                .deptId(deptId)
                .build());
        return Result.ok();
    }

    @Override
    public Result<?> checkEntry() {
        UserInfo userInfo = userInfoMapper.selectById(UserThreadHolder.getCurrentUser().getId());
        User user = userMapper.selectById(UserThreadHolder.getCurrentUser().getId());
        HashMap<Object, Object> map = new HashMap<>();
        map.put("role", user.getRole());
        map.put("rights", user.getRights());
        map.put("status", userInfo.getStatus());
        return Result.ok(JSON.parseObject(JSON.toJSONString(map)));
    }

}
