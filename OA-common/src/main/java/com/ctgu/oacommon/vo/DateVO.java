package com.ctgu.oacommon.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhang Jinming
 * @date 17/8/2022 下午4:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DateVO {
    @JSONField(format = "yyyy-MM-dd")
    private Date time;
}
