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
@ApiModel(description = "员工打卡日志")
public class CheckDTO {
    /**
     * 日志ID
     */
    private Long checkId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 打卡时间
     */
    private Date time;

    /**
     * 打卡类型
     */
    private Integer type;
}
