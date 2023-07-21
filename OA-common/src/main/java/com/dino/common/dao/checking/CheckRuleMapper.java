package com.dino.common.dao.checking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dino.common.entity.CheckRule;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Li Zihan
 */
@Mapper
@Repository
public interface CheckRuleMapper extends BaseMapper<CheckRule> {
}
