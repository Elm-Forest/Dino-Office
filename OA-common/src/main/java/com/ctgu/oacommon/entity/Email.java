package com.ctgu.oacommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Elm Forest
 * @TableName email
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "email")
@Data
public class Email implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 发送方用户编号
     */
    @TableField(value = "su_id")
    private Long suId;

    /**
     * 接受方用户编号
     */
    @TableField(value = "au_id")
    private Long auId;

    /**
     * 邮件主题
     */
    @TableField(value = "email_title")
    private String emailTitle;

    /**
     * 邮件内容
     */
    @TableField(value = "email_content")
    private String emailContent;

    /**
     * 附件的完整存储路径
     */
    @TableField(value = "attachment_path")
    private String attachmentPath;

    /**
     * 发送时间
     */
    @TableField(value = "send_time")
    private Date sendTime;

    /**
     * 对于发送者1-收件箱，发件箱，2-草稿箱，3-废件箱 4-用户彻底删除
     */
    @TableField(value = "send_email_type")
    private Integer sendEmailType;

    /**
     * 对于接收者1-收件箱，发件箱，2-草稿箱，3-废件箱
     */
    @TableField(value = "accept_email_type")
    private Integer acceptEmailType;

    //发送者用户名
    @TableField(exist = false)
    private String sendUserName;
    //接收者用户名
    @TableField(exist = false)
    private String acceptUserName;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}