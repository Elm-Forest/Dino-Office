package com.dino.user.service.core;

import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.AuthVO;
import com.dino.common.models.vo.UserVO;

/**
 * @author Zhang Jinming
 */
public interface AuthService {

    /**
     * 登录
     *
     * @param authVO 登录信息
     * @return Result
     */
    Result<?> login(AuthVO authVO);

    /**
     * 注册
     *
     * @param user 用户信息
     * @return Result
     */
    Result<?> register(UserVO user);

    /**
     * 登出
     *
     * @return Result
     */
    Result<?> logout();

    /**
     * 发送验证码
     *
     * @param email 邮箱
     */
    void sendCode(String email);
}
