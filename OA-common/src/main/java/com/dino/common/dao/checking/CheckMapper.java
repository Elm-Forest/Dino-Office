package com.dino.common.dao.checking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dino.common.entity.Check;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Li Zihan
 */
@Mapper
@Repository
public interface CheckMapper extends BaseMapper<Check> {
}
