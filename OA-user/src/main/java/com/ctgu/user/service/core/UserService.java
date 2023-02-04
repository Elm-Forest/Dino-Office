package com.ctgu.user.service.core;

import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.UserVO;

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
