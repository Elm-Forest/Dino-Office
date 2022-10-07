package com.ctgu.oacommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 16/8/2022 上午10:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RsaDTO {

    private String password;

    private String salt;
}
