package com.ctgu.oauser.service.common;

import com.ctgu.oacommon.entity.Department;
import com.ctgu.oacommon.entity.User;
import com.ctgu.oacommon.vo.UserInfoVO;
import com.ctgu.oacommon.vo.UserVO;

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

    List<Department> selectDepartmentByName(String name);

    void sendCode(String email, String msg);

    /**
     * 根据用户名称来查找用户id
     * @return 用户id
     */
    List<User> getUserIdByUserName(String username);
    /**
     * 根据用户名称来查找用户id
     * @return 用户id
     */
    String getUserNameByUserId(Long id);

}
