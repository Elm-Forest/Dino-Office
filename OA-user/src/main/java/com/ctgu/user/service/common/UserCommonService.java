package com.ctgu.user.service.common;

import com.ctgu.common.entity.Department;
import com.ctgu.common.entity.User;
import com.ctgu.common.models.vo.UserInfoVO;
import com.ctgu.common.models.vo.UserVO;

import java.util.List;

/**
 * @author Zhang Jinming
 * @date 15/8/2022 下午9:27
 */
public interface UserCommonService {
    User selectUserByUserName(String username);

    void checkActivated(Integer status);

    Boolean checkUser(UserVO userVO);

    Boolean checkEmail(String email);

    int updateUserInfoById(UserInfoVO user);

    int insertUserInfoById(UserInfoVO user);

    void sendCode(String email, String msg);

    /**
     * 根据用户名称来查找用户id
     *
     * @param username 用户名
     * @return 用户id
     */
    List<User> getUserIdByUserName(String username);

    /**
     * 根据用户id来查找username
     *
     * @param id id
     * @return username
     */
    String getUserNameByUserId(Long id);

}
