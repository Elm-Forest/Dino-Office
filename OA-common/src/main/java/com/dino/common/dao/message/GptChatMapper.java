package com.dino.common.dao.message;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dino.common.entity.Chat;
import com.dino.common.entity.GptChat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Zhang Jinming
 * @date 7/7/2023 9:47 PM
 */
@Mapper
public interface GptChatMapper extends BaseMapper<GptChat> {

    @Select(value = {"""
            SELECT DISTINCT chat.*
            FROM chat
            WHERE chat.id IN (
                SELECT gpt_chat.chat_id
                FROM gpt_chat
                WHERE gpt_chat.session_id = #{session_id}
            )
            ORDER BY chat.time
            """})
    List<Chat> selectGptChats(@Param("session_id") String sessionId);
}
