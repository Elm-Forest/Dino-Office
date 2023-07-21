package com.dino.common.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Elm Forest
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
    private Long id;

    private Long suId;

    private String sName;

    private Long auId;

    private String aName;

    private String content;

    private Date time;

    private Boolean isSelf;
}