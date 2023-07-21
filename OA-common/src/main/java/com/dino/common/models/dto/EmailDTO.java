package com.dino.common.models.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhang Jinming
 * @date 2/12/2023 3:39 AM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "邮件信息")
public class EmailDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 发送方用户编号
     */
    private Long suId;

    /**
     * 接受方用户编号
     */
    private Long auId;

    /**
     * 邮件主题
     */
    private String emailTitle;

    /**
     * 邮件内容
     */
    private String emailContent;

    /**
     * 附件的完整存储路径
     */
    private String attachmentPath;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 对于发送者1-收件箱，发件箱，2-草稿箱，3-废件箱 4-用户彻底删除
     */
    private Integer sendEmailType;

    /**
     * 对于接收者1-收件箱，发件箱，2-草稿箱，3-废件箱
     */
    private Integer acceptEmailType;

    /**
     * 发送者用户名
     */
    private String sendUserName;
    /**
     * 接收者用户名
     */
    private String acceptUserName;
}
