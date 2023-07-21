package com.dino.common.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dino.common.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

/**
 * 用户mapper
 *
 * @author Zhang Jinming
 * &#064;create  10/8/2022 下午9:59
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户名称来查找用户id
     *
     * @param name 用户名称
     * @return 用户id
     */
    Long getUserIdByUserName(@Param("name") String name);

    /**
     * 根据用户名称来查找用户id
     *
     * @param id 用户id
     * @return 用户id
     */
    String getUserNameByUserId(@Param("id") Long id);
}
