package com.ctgu.common.models.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 李嘉超
 * @version 1.0
 * @date 2022/8/16 15:58
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "部门")
public class DepartmentVO {

    /**
     * 部门ID
     */
    private Long id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门联系电话
     */
    private String phone;

    /**
     * 部门地址
     */
    private String address;

    /**
     * 部门头像
     */
    private String headImg;

    /**
     * 部门描述
     */
    private String desc;
}
