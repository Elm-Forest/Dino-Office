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
import java.util.Date;

/**
 * 
 * @author Elm Forest
 * @TableName ignore_attendance_user
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="ignore_attendance_user")
@Data
public class IgnoreAttendanceUser implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 用户编号
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 开始时间
     */
    @TableField(value = "begin_time")
    private Date beginTime;

    /**
     * 结束时间(在这段时间里面此用户不用考勤)
     */
    @TableField(value = "end_time")
    private Date endTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}