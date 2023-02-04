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
 * 
 * @author Elm Forest
 * @TableName attendance
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="attendance")
@Data
public class Attendance implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 考勤时间
     */
    @TableField(value = "attendance_time")
    private Date attendanceTime;

    /**
     * 考勤用户编号
     */
    @TableField(value = "user_Id")
    private Long userId;

    /**
     * 是否签到(1-已签到，0-未签到)
     */
    @TableField(value = "sign_in")
    private Integer signIn;

    /**
     * 是否签退(1-已签退，0-未签退)
     */
    @TableField(value = "sign_back")
    private Integer signBack;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}