package com.ctgu.oacommon.entity;

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

    @TableId(value = "id")
    private Long id;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "note_content")
    private String noteContent;
}
