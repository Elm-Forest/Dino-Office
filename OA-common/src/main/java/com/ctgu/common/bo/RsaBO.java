package com.ctgu.common.bo;

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
public class RsaBO {

    private String password;

    private String salt;
}
