package com.dino.common.models.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 7/6/2023 8:03 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "消息提醒")
public class NotificationDTO {
    private String id;

    private String suName;

    private Long suId;

    private Long auId;

    private String content;
}
