package com.ctgu.common.models.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 21/8/2022 下午3:19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "员工视图")
public class EmployeeVO {
    @JSONField(serializeUsing = ToStringSerializer.class)
    private String id;

    private Long deptId;

    private String name;

    private Integer sex;

    private String address;

    private String phone;

    private String email;

    private Integer role;

    private Integer rights;

    private Integer status;
}
