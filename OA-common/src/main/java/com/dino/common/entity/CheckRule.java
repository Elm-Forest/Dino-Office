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
 * @author 李木子
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="check_rule")
@Data
public class CheckRule implements Serializable{
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 部门编号
     */
    @TableField(value = "department_id")
    private Long departmentId;

    /**
     * 起始时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 终止时间
     */
    @TableField(value = "end_time")
    private Date endTime;

}
