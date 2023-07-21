package com.dino.common.models.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 7/5/2023 3:44 AM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageVO {
    private Long suId;
    private Long auId;
    private String content;
}
