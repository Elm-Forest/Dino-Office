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
 * @TableName document_log
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "document_log")
@Data
public class DocumentLog implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 操作者
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 文档所属部门
     */
    @TableField(value = "dept_id")
    private Long deptId;

    /**
     * 此次操作的文档编号
     */
    @TableField(value = "document_id")
    private Long documentId;

    /**
     * 1-添加,2-查看，3-修改，4-删除，5-恢复，6-彻底删除
     */
    @TableField(value = "operation")
    private Integer operation;

    /**
     * 操作时间
     */
    @TableField(value = "operation_time")
    private Date operationTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}