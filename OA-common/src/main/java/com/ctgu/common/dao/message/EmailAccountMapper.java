package com.ctgu.common.dao.message;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctgu.common.entity.EmailAccount;
import com.ctgu.common.models.dto.ContactsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 邮件账户mapper
 *
 * @author Zhang Jinming
 * @date 19/8/2022 下午5:32
 */
@Mapper
public interface EmailAccountMapper extends BaseMapper<EmailAccount> {

    /**
     * 查询联系人
     *
     * @param deptId 部门id
     * @param id     用户id
     * @param name   用户名
     * @return 联系人列表
     */
    @Select(value = {"""
            SELECT user_info.id,user_info.name,email_account.email\s
            FROM user_info INNER JOIN email_account ON user_info.id = email_account.id\s
            WHERE user_info.dept_id = #{deptId}\s
            AND user_info.id != #{id}\s
            AND (user_info.name LIKE concat('%', #{name}, '%') OR #{name} IS NULL)
            """})
    List<ContactsDTO> selectContacts(@Param("deptId") Long deptId,
                                     @Param("id") Long id,
                                     @Param("name") String name);
}
