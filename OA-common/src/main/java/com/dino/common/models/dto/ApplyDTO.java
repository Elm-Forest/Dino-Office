package com.dino.common.models.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "员工补卡申请")
public class ApplyDTO {
    /**
     * 日志ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 申请起始时间
     */
    private Date applyStartTime;

    /**
     * 申请截止时间
     */
    private Date applyEndTime;

    /**
     * 申请类型
     */
    private Integer type;

    /**
     * 申请描述
     */
    private String applyDes;
}
