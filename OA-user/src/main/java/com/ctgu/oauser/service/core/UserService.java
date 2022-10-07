package com.ctgu.oauser.service.core;

import com.ctgu.oacommon.vo.Result;
import com.ctgu.oacommon.vo.UserVO;

/**
 * @author Zhang Jinming
 * &#064;create  10/8/2022 下午10:03
 */
public interface UserService {

    Result<?> selectUser();

    Result<?> forgetPassword(UserVO user);

    void sendCode(String email);

    Result<?> updatePassword(String newPassword,String code);

    void autoSendCode();
}
