package com.dino.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 7/7/2023 9:45 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("gpt_chat")
public class GptChat {
    @TableField(value = "session_id")
    private String sessionId;

    @TableField(value = "chat_id")
    private Long chatId;
}
