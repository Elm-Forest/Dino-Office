package com.dino.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Li Zihan
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "note")
@Data
public class Note {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户编号
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 便签标题
     */
    @TableField(value = "note_title")
    private String noteTitle;

    /**
     * 便签内容
     */
    @TableField(value = "note_content")
    private String noteContent;
}
