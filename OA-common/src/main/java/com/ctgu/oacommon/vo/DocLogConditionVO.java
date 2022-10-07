package com.ctgu.oacommon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午9:02
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("日志条件查询")
@Data
public class DocLogConditionVO {
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
     * 操作者
     */
    private String name;

    /**
     * 此次操作的文档名称
     */
    private String documentName;

    /**
     * 1-添加,2-查看，3-修改，4-删除，5-恢复，6-彻底删除
     */
    private Integer operation;

    /**
     * 起始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;
}
