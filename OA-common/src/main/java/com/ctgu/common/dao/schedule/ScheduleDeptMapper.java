package com.ctgu.common.dao.schedule;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctgu.common.entity.ScheduleDepartment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Li Zihan
 */
@Mapper
@Repository
public interface ScheduleDeptMapper extends BaseMapper<ScheduleDepartment> {
}
