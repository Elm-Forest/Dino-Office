package com.ctgu.oauser.service.core;

import com.ctgu.oacommon.vo.AuthVO;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oacommon.vo.UserVO;

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
