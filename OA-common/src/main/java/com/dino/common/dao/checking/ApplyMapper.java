package com.dino.common.dao.checking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dino.common.entity.Apply;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ApplyMapper extends BaseMapper<Apply> {
}
