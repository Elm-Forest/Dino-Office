package com.ctgu.common.models.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午2:35
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "文档")
public class DocumentVO {

    private String id;

    private Long deptId;

    /**
     * 创建者id
     */
    private Long createId;

    private String createName;
    /**
     * 修改者id
     */
    private Long modifyId;

    private String modifyName;

    private String name;

    private String extension;

    private Long size;

    private Integer type;

    private String url;

    private String filePath;

    private Date modifyTime;

    private Date createTime;
}
