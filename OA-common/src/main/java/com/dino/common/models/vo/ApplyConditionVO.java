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
@ApiModel(description = "条件查询补卡申请")
public class ApplyConditionVO {
    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 起始时间
     */
    private String startTime;

    /**
     * 终止时间
     */
    private String endTime;
}
