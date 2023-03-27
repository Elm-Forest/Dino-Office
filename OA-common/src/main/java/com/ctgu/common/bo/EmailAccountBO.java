package com.ctgu.common.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮箱账号信息
 *
 * @author Zhang Jinming
 * @date 20/8/2022 下午2:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailAccountBO {
    private String email;
    private String password;
    private String host;
    private Integer port;
    private String protocol;
}
