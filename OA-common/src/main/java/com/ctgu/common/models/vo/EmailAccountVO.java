package com.ctgu.common.models.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 19/8/2022 下午5:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "邮箱账户")
public class EmailAccountVO {

    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮箱授权码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     *
     */
    private Integer url;
}
