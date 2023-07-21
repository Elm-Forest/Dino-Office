package com.dino.common.models.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author 李嘉超
 * @version 1.0
 * @date 2022/8/16 16:41
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "消息")
public class MessageVO {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 发送用户编号
     */
    private Long suId;

    /**
     * 接受用户编号
     */
    private Long auId;

    /**
     * 消息标题
     */
    private String messageHeader;

    /**
     * 消息内容
     */
    private String messageContent;


    // private String messageTime; // 发送时间,自动生成，不用这个属性

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
    /**
     * 有效时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String messageValidTime;
}
