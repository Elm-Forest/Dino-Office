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
 * @date 7/5/2023 3:39 AM
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "chat")
@Data
public class Chat {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 发送用户编号
     */
    @TableField(value = "su_id")
    private Long suId;

    /**
     * 接受用户编号
     */
    @TableField(value = "au_id")
    private Long auId;

    /**
     * 发送时间
     */
    @TableField(value = "time")
    private Date time;


    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;
}
