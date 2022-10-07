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
import java.sql.Time;
import java.util.Date;

/**
 * 
 * @author Elm Forest
 * @TableName attendance_role
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="attendance_role")
@Data
public class AttendanceRole implements Serializable {
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
     * 考勤日期
     */
    @TableField(value = "attendance_time")
    private Date attendanceTime;

    /**
     * 规定上班时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 规定下班时间
     */
    @TableField(value = "back_time")
    private Date backTime;

    /**
     * 签到实际开始时间
     */
    @TableField(value = "begin_time")
    private Date beginTime;

    /**
     * 签到实际结束时间（在这段时间里面按照上面的时间段来作为判断是否迟到，早退的依据时间）
     */
    @TableField(value = "end_time")
    private Date endTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}