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
@ApiModel(description = "补签与请假")
public class ApplyVO {
    /**
     * 申请起始时间
     */
    private String applyStartTime;

    /**
     * 申请终止时间
     */
    private String applyEndTime;

    /**
     * 申请类型
     */
    private Integer type;

    /**
     * 申请描述
     */
    private String des;
}
