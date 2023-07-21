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
@ApiModel(description = "考勤规则")
public class CheckRuleVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 部门编号
     */
    private Long departmentId;

    /**
     * 起始时间
     */
    private String startTime;

    /**
     * 终止时间
     */
    private String endTime;
}
