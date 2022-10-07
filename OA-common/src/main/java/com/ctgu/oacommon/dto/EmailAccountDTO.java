package com.ctgu.oacommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 20/8/2022 下午2:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailAccountDTO {
    private String email;
    private String password;
    private String host;
    private Integer port;
    private String protocol;
}
