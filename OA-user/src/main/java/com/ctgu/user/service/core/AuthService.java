package com.ctgu.user.service.core;

import com.ctgu.common.models.vo.AuthVO;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.UserVO;

/**
 * @author Zhang Jinming
 * @date 15/8/2022 下午8:35
 */
public interface AuthService {

    Result<?> login(AuthVO authVO);

    Result<?> register(UserVO user);

    Result<?> logout();

    void sendCode(String email);
}
