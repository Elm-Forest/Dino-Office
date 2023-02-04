package com.ctgu.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Elm Forest
 * @TableName recycle
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "recycle")
@Data
public class Recycle implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 删除者id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 删除时间
     */
    @TableField(value = "delete_time")
    private Date deleteTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}