package com.ctgu.oadepartment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.oacommon.entity.UserInfo;
import com.ctgu.oacommon.vo.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Zhang Jinming
 * @date 21/8/2022 下午3:16
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<UserInfo> {
    Page<EmployeeVO> selectEmployee(@Param("page") Page<EmployeeVO> page,
                                    @Param("deptId") Long deptId,
                                    @Param("status") Integer status,
                                    @Param("name") String name,
                                    @Param("role") Integer role,
                                    @Param("rights") Integer rights);
}
