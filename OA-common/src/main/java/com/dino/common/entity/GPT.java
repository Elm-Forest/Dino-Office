package com.dino.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhang Jinming
 * @date 7/5/2023 11:33 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("gpt")
public class GPT {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "session_id")
    private String sessionId;

    @TableField(value = "title")
    private String title;

    @TableField(value = "session_content")
    private String sessionContent;

    @TableField(value = "time")
    private Date time;
}
