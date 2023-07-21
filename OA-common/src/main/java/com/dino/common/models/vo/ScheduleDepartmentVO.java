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
@ApiModel(description = "部门日程表")
public class ScheduleDepartmentVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 部门编号
     */
    private Long deptId;

    /**
     * 日程标题
     */
    private String scheduleTitle;

    /**
     * 日程内容
     */
    private String scheduleContent;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

}
