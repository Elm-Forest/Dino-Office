package com.ctgu.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhang Jinming
 * &#064;create  10/8/2022 下午9:59
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    private String password;

    private String salt;

    private String email;

    @TableField(fill = FieldFill.INSERT)
    private Integer status;

    private Integer role;

    private Integer rights;

    @TableField(value = "reg_time")
    private Date regTime;

    @TableField(value = "last_time")
    private Date lastTime;
}
