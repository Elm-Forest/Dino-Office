package com.ctgu.oacommon.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhang Jinming
 * @date 19/8/2022 上午10:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "文件日志结果")
public class DocLogVO {
    private Long id;
    /**
     * 文档名称
     */
    private String name;
    /**
     * 修改人
     */
    private String modifyName;

    private String extension;
    private String type;
    private String filePath;
    private Long size;
    private Integer operation;
    private Date operationTime;
}
