package com.dino.common.models.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 李嘉超
 * @version 1.0
 * @date 2022/8/19 16:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "消息条件查询")
public class MessageConditionVO {
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

    /**
     * 接收者或者发送者的用户名
     */
    private String asUserName;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;
    //查找发送时间上面两个时间组成的一个时间段里面的消息
    //这只会限定于查询已发送消息，而对于未发送消息就没有这个条件查询了

}
