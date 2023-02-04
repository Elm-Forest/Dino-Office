package com.ctgu.common.models.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhang Jinming
 * @date 1/24/2023 8:44 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "Auth信息")
public class AuthInfoDTO {
    private Long id;

    private String username;

    private String email;

    private Integer role;

    private Integer rights;

    private Date regTime;

    private Date lastTime;
}
