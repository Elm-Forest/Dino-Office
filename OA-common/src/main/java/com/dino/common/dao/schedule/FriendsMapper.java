package com.dino.common.dao.schedule;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dino.common.entity.Friends;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Li Zihan
 */
@Mapper
@Repository
public interface FriendsMapper extends BaseMapper<Friends> {
}
