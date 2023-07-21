package com.dino.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hqing
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="apply")
@Data
public class Apply {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户编号
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 申请起始时间
     */
    @TableField(value = "apply_start_time")
    private Date applyStartTime;

    /**
     * 申请终止时间
     */
    @TableField(value = "apply_end_time")
    private Date applyEndTime;

    /**
     * 申请类型
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 申请描述
     */
    @TableField(value = "apply_des")
    private String applyDes;
}
