package com.ctgu.common.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Elm Forest
 * @TableName attendance_statistics
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "attendance_statistics")
@ColumnWidth(20)
@ContentRowHeight(20)
@HeadRowHeight(30)
@Data
@ExcelIgnoreUnannotated
public class AttendanceStatistics implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 用户编号
     */
    @ExcelProperty(value = "用户编号", index = 0)
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 出勤率
     */
    @ExcelProperty(value = "用户出勤率", index = 1)
    @TableField(value = "attendance")
    private Double attendance;

    /**
     * 迟到次数
     */
    @ExcelProperty(value = "迟到次数", index = 2)
    @TableField(value = "late_count")
    private Integer lateCount;

    /**
     * 早退次数
     */
    @ExcelProperty(value = "早退次数", index = 3)
    @TableField(value = "early_count")
    private Integer earlyCount;

    /**
     * 旷工次数
     */
    @ExcelProperty(value = "旷工次数", index = 4)
    @TableField(value = "absenteeism_count")
    private Integer absenteeismCount;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}