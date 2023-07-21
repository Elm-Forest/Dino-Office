package com.dino.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @TableName email_account
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "email_account")
@Data
public class EmailAccount implements Serializable {
    /**
     * 用户id，关联user.id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 邮箱授权码
     */
    @TableField(value = "password")
    private String password;
    /**
     * 邮件服务器
     */
    @TableField(value = "host")
    private String host;
    /**
     * 端口
     */
    @TableField(value = "port")
    private Integer port;
    /**
     * 协议
     */
    @TableField(value = "protocol")
    private String protocol;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}