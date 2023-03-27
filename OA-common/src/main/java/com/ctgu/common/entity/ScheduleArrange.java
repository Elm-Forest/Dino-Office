package com.ctgu.common.entity;

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
import java.util.Date;

/**
 * @author Li Zihan
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "schedule_arrange")
@Data
public class ScheduleArrange implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户编号
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 日程标题
     */
    @TableField(value = "schedule_title")
    private String scheduleTitle;

    /**
     * 日程内容
     */
    @TableField(value = "schedule_content")
    private String scheduleContent;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 开始时间
     */
    @TableField(value = "begin_time")
    private String beginTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private String endTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}