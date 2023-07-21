package com.dino.common.models.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "便签")
public class NoteVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 便签标题
     */
    private String noteTitle;

    /**
     * 便签内容
     */
    private String noteContent;
}
