package com.dino.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Li Zihan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("friends")
public class Friends {

    @TableId(value = "id")
    private Long id;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "friend_id")
    private Long friendId;
}
