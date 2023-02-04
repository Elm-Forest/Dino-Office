package com.ctgu.common.models.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 16/8/2022 下午5:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户详细信息")
public class UserInfoVO {
    private Long id;

    private Long deptId;

    private String deptName;

    private String name;

    private Integer sex;

    private String address;

    private String phone;

    private String email;

    private String headImg;
}
