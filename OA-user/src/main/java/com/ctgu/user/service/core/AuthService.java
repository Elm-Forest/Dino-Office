package com.ctgu.user.service.core;

import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.AuthVO;
import com.ctgu.common.models.vo.UserVO;

/**
 * @author Zhang Jinming
 * @date 15/8/2022 下午8:35
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
