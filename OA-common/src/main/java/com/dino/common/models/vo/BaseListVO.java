package com.dino.common.models.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 2/12/2023 1:59 AM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "带分页的请求，如果没有指定条件，则采用此视图")
public class BaseListVO {
    private Integer current;
    private Integer size;
}
