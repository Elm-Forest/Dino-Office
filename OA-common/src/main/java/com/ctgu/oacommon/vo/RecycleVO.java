package com.ctgu.oacommon.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午5:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "回收站")
public class RecycleVO {
    private Long id;

    private Long deptId;

    private Long deleteId;

    private Long dName;

    private String name;

    private String extension;

    private Long size;

    private Integer type;

    private String filePath;

    private Date deleteTime;
}
