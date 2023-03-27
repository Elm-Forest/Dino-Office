package com.ctgu.user.service.common;

import com.ctgu.common.entity.User;
import com.ctgu.common.models.vo.UserInfoVO;
import com.ctgu.common.models.vo.UserVO;

import java.util.List;

/**
 * 用户公共服务
 *
 * @author Zhang Jinming
 * @date 15/8/2022 下午9:27
 */
public interface UserCommonService {
    /**
     * 根据用户名来查找用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User selectUserByUserName(String username);

    /**
     * 检查用户是否激活
     *
     * @param status 用户状态
     */
    void checkActivated(Integer status);

    /**
     * 检查用户是否存在
     *
     * @param userVO 用户信息
     * @return 是否存在
     */
    Boolean checkUser(UserVO userVO);


    /**
     * 根据用户id来更新用户信息
     *
     * @param user 用户信息
     * @return 更新结果
     */
    int updateUserInfoById(UserInfoVO user);

    /**
     * 根据用户id来插入用户信息
     *
     * @param user 用户信息
     * @return 插入结果
     */
    int insertUserInfoById(UserInfoVO user);

    /**
     * 发送验证码
     *
     * @param email 邮箱
     * @param msg   验证码
     */
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
