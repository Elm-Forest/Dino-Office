package com.ctgu.oacommon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午9:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "文件条件查询")
public class DocumentConditionVO {
    /**
     * 页码
     */
    @ApiModelProperty(name = "current", value = "页码", dataType = "Long", example = "1")
    private Integer current;

    /**
     * 条数
     */
    @ApiModelProperty(name = "size", value = "条数", dataType = "Long", example = "1")
    private Integer size;

    /**
     * 上一次修改者
     */
    private String uName;

    /**
     * 此次操作的文档名称
     */
    private String dName;

    /**
     * 起始的上一次修改时间
     */
    private String beginTime;

    /**
     * 结束的上一次修改时间
     */
    private String endTime;
}
