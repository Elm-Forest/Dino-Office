package com.ctgu.oacommon.entity;

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
 * @author Elm Forest
 * @TableName user_info
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_info")
@Data
public class UserInfo implements Serializable {
    /**
     * 主键，关联user.id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 部门id
     */
    @TableField(value = "dept_id")
    private Long deptId;

    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 性别[{女:0}{男:1}]
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 所在地
     */
    @TableField(value = "address")
    private String address;

    /**
     * 联系电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 联系邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 入职状态;
     * 0:未入职;
     * 1:在职;
     * 2:离职
     */
    @TableField(value = "status")
    private Integer status;
    /**
     * 头像链接
     */
    @TableField(value = "head_img")
    private String headImg;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}