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

/**
 * 
 * @author Elm Forest
 * @TableName role
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="role")
@Data
public class Role implements Serializable {
    /**
     * 关联user.role字段
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 角色名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 角色描述
     */
    @TableField(value = "desc")
    private String desc;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}