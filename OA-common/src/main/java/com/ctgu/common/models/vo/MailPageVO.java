package com.ctgu.common.models.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @date 19/8/2022 下午10:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "邮件正文主体")
public class MailPageVO {
    private String emailAddress;
    private Long auId;
    private String subject;
    private String content;
}
