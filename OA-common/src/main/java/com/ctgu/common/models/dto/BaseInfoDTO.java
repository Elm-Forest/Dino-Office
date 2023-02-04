package com.ctgu.common.models.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 2/1/2023 6:18 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "用于主页展示的基础信息")
public class BaseInfoDTO {
    private String userName;

    private Integer role;

    private String deptName;

    private String deptUrl;
}
