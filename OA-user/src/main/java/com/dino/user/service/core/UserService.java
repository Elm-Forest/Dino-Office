package com.dino.user.service.core;

import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.UserVO;

/**
 * 用户服务
 *
 * @author Zhang Jinming
 */
public interface UserService {

    /**
     * 查询用户信息
     *
     * @return Result
     */
    Result<?> selectUser();

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return Result
     */
    Result<?> forgetPassword(UserVO user);

    /**
     * 更新用户密码
     *
     * @param email 邮箱
     */
    void sendPasswordCode(String email);

    /**
     * 更新用户密码
     *
     * @param newPassword 新密码
     * @param code        验证码
     * @return Result
     */
    Result<?> updatePassword(String newPassword, String code);

    /**
     * 自动发送验证码
     */
    void autoSendCode();

    /**
     * 更新用户邮箱
     *
     * @param email 邮箱
     * @param code  验证码
     * @return Result
     */
    Result<?> updateEmail(String email, String code);

    /**
     * 发送验证码
     *
     * @param email 邮箱
     */
    void sendEmailCode(String email);
}
