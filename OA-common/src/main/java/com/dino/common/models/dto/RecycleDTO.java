package com.dino.common.models.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 回收站
 *
 * @author Zhang Jinming
 * @date 18/8/2022 下午5:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "回收站")
public class RecycleDTO {
    private Long id;

    private Long deptId;

    private Long deleteId;

    private String delName;

    private String name;

    private String extension;

    private Long size;

    private Integer type;

    private String filePath;

    private Date deleteTime;
}
