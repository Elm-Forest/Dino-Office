package com.ctgu.oacommon.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author Zhang Jinming
 * @date 16/8/2022 上午9:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户")
public class UserVO {
    private Long id;

    private String username;

    private String password;

    private String email;

    private Integer role;

    private Integer rights;

    @JSONField(format = "yyyy-MM-dd")
    private Date regTime;

    @JSONField(format = "yyyy-MM-dd")
    private Date lastTime;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(name = "code", value = "邮箱验证码", required = true, dataType = "String")
    private String code;
}
