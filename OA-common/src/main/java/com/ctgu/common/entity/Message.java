package com.ctgu.common.entity;

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
 * @TableName message
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "message")
@Data
public class Message implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发送用户编号
     */
    @TableField(value = "su_id")
    private Long suId;

    /**
     * 接受用户编号
     */
    @TableField(value = "au_id")
    private Long auId;

    /**
     * 消息标题
     */
    @TableField(value = "message_header")
    private String messageHeader;

    /**
     * 消息内容
     */
    @TableField(value = "message_content")
    private String messageContent;

    /**
     * 发送时间
     */
    @TableField(value = "message_time")
    private Date messageTime;

    /**
     * 消息类型，1表示已发送，0表示未发送
     */
    @TableField(value = "message_type")
    private int messageType;

    /**
     * 消息有效时间，没有的话就是默认没有有效时间，永久有效
     */
    @TableField(value = "message_valid_time")
    private Date messageValidTime;

    /**
     * 消息是否过期,1表示过期，0表示没过期
     */
    @TableField(value = "message_overdue")
    private int messageOverdue;

    /**
     * 发送者用户名
     */
    @TableField(exist = false)
    private String sendUserName;

    /**
     * 接收者用户名
     */
    @TableField(exist = false)
    private String acceptUserName;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}