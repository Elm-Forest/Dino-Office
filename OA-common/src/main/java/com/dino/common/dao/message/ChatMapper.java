package com.dino.common.dao.message;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dino.common.entity.Chat;
import com.dino.common.models.dto.ChatDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Zhang Jinming
 * @date 7/5/2023 3:47 AM
 */
@Mapper
public interface ChatMapper extends BaseMapper<Chat> {
    @Select(value = {"""
            SELECT DISTINCT chat.*, user_info1.name AS a_name, user_info2.name AS s_name\s
            FROM chat\s
            LEFT JOIN user_info AS user_info1 ON chat.au_id = user_info1.id\s
            LEFT JOIN user_info AS user_info2 ON chat.su_id = user_info2.id\s
            WHERE (chat.su_id = #{suId} AND chat.au_id = #{auId})\s
            OR (chat.su_id = #{auId} AND chat.au_id = #{suId})
            """})
    List<ChatDTO> selectChats(@Param("suId") Long suId,
                              @Param("auId") Long auId);
}
