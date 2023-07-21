package com.dino.common.models.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 21/8/2022 下午3:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "员工条件视图")
public class EmployeeConditionVO {
    /**
     * 页码-第几页
     */
    @ApiModelProperty(name = "current", value = "页码", dataType = "Long", example = "1")
    private Integer current;

    /**
     * 当页条数
     */
    @ApiModelProperty(name = "size", value = "条数", dataType = "Long", example = "1")
    private Integer size;

    private Integer status;

    private String name;

    private Integer role;

    private Integer rights;

}
