package com.ctgu.user.service.core;

import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户信息服务
 *
 * @author Zhang Jinming
 * @date 16/8/2022 下午3:33
 */
public interface UserInfoService {
    /**
     * 查询用户信息
     *
     * @return Result
     */
    Result<?> selectUserInfo();

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return Result
     */
    Result<?> updateUserInfo(UserInfoVO user);

    /**
     * 插入用户信息
     *
     * @param user 用户信息
     * @return Result
     */
    Result<?> insertUserInfo(UserInfoVO user);

    /**
     * 更新用户头像
     *
     * @param headImg 头像
     * @return Result
     */
    Result<?> updateUserHeadImg(MultipartFile headImg);

    /**
     * 查询用户头像
     *
     * @return Result
     */
    Result<?> selectUserHeadImg();

    /**
     * 查询用户部门
     *
     * @param deptId 部门id
     * @return Result
     */
    Result<?> entryApply(Long deptId);

    /**
     * 查询用户部门
     *
     * @return Result
     */
    Result<?> checkEntry();

    /**
     * 查询用户部门
     *
     * @return Result
     */
    Result<?> selectBaseInfo();
}
