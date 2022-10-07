package com.ctgu.oauser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctgu.oacommon.entity.Department;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Zhang Jinming
 * @date 16/8/2022 下午6:16
 */
@Mapper
public interface UserDepartmentMapper extends BaseMapper<Department> {
}
