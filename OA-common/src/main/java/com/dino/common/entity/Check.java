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
@TableName(value ="check_list")
@Data
public class Check implements Serializable {
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
     * 考勤规则编号
     */
    @TableField(value = "check_rule_id")
    private Long checkRuleId;

    /**
     * 考勤类型
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 考勤时间
     */
    @TableField(value = "time")
    private Date time;
}
