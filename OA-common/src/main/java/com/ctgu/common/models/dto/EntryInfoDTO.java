package com.ctgu.common.models.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 1/24/2023 8:57 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "Entry信息")
public class EntryInfoDTO {
    private Integer role;

    private Integer rights;

    private Integer status;
}
