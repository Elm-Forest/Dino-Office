package com.ctgu.oaschedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctgu.oacommon.entity.ScheduleDepartment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Li Zihan
 */
@Mapper
@Repository
public interface ScheduleDeptMapper extends BaseMapper<ScheduleDepartment> {
}
