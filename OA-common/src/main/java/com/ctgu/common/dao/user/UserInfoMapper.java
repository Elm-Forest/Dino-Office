package com.ctgu.common.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.common.entity.UserInfo;
import com.ctgu.common.models.vo.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Zhang Jinming
 * @date 16/8/2022 上午10:02
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    Page<EmployeeVO> selectEmployee(@Param("page") Page<EmployeeVO> page,
                                    @Param("deptId") Long deptId,
                                    @Param("status") Integer status,
                                    @Param("name") String name,
                                    @Param("role") Integer role,
                                    @Param("rights") Integer rights);
}
