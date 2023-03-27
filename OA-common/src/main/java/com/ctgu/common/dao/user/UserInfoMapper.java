package com.ctgu.common.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.common.entity.UserInfo;
import com.ctgu.common.models.dto.EmployeeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户信息mapper
 *
 * @author Zhang Jinming
 * @date 16/8/2022 上午10:02
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    /**
     * 查询员工
     *
     * @param page   分页
     * @param deptId 部门id
     * @param status 状态
     * @param name   姓名
     * @param role   角色
     * @param rights 权限
     * @return 员工列表
     */
    Page<EmployeeDTO> selectEmployee(@Param("page") Page<EmployeeDTO> page,
                                     @Param("deptId") Long deptId,
                                     @Param("status") Integer status,
                                     @Param("name") String name,
                                     @Param("role") Integer role,
                                     @Param("rights") Integer rights);
}
