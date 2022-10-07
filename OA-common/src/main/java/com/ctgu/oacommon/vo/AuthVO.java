package com.ctgu.oacommon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Zhang Jinming
 * @date 15/8/2022 下午9:08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "登录与认证")
public class AuthVO {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(name = "username", value = "用户名或邮箱", required = true, dataType = "String")
    private String username;


    /**
     * 密码
     */
    @Size(min = 6, message = "密码不能少于6位")
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(name = "password", value = "密码", required = true, dataType = "String")
    private String password;
}
