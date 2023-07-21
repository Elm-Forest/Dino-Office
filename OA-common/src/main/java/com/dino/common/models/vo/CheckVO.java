package com.dino.common.models.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "考勤日志")
public class CheckVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 考勤规则编号
     */
    private Long checkRuleId;

    /**
     * 考勤类型
     */
    private Integer type;

    /**
     * 考勤时间
     */
    private String time;
}
