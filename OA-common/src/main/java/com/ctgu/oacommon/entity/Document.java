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
 * @TableName document
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "document")
@Data
public class Document implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文件所属部门编号
     */
    @TableField(value = "dept_id")
    private Long deptId;

    /**
     * 创建者编号(用户编号)
     */
    @TableField(value = "create_id")
    private Long createId;

    /**
     * 修改者编号(用户编号)
     */
    @TableField(value = "modify_id")
    private Long modifyId;

    /**
     * 文件名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 文件扩展名
     */
    @TableField(value = "extension")
    private String extension;

    /**
     * 文件大小
     */
    @TableField(value = "size")
    private Long size;

    /**
     * 文件类型，1:文件,2:文件夹
     */
    @TableField(value = "type")
    private Integer type;
    /**
     * 文件下载地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 文件存储路径
     */
    @TableField(value = "file_path")
    private String filePath;

    /**
     * 修改时间
     */
    @TableField(value = "modify_time")
    private Date modifyTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}