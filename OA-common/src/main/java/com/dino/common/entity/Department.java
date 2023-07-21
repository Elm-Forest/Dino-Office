package com.dino.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Jinming
 * @create 15/8/2022 上午11:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("department")
public class Department {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private String phone;

    private String address;

    @TableField(value = "head_img")
    private String headImg;

    @TableField("`describe`")
    private String describe;
}
