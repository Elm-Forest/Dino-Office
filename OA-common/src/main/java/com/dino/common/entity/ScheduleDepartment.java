package com.dino.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Li Zihan
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "schedule_department")
@Data
public class ScheduleDepartment implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 部门编号
     */
    @TableField(value = "dept_id")
    private Long deptId;

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
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

}
