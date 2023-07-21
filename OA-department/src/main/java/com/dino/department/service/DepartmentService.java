package com.dino.department.service;

import com.dino.common.entity.Department;
import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.DepartmentVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 部门服务
 *
 * @author 李嘉超
 * @author zhang jinming
 * @version 1.0
 */
public interface DepartmentService {
    /**
     * 根据ID查询对应的部门所有信息
     *
     * @return 部门的全部信息
     */
    Result<Department> selectDepartment();

    /**
     * 插入一个部门
     *
     * @param departmentVO 前端传来的部门VO对象
     * @return 是否插入成功  true 插入成功   false  插入失败
     */
    Result<?> insertDepartment(DepartmentVO departmentVO);

    /**
     * 更新部门信息
     *
     * @param departmentVO 前端传来的更新后的部门VO对象
     * @return 是否插入成功  true 插入成功   false  插入失败
     */
    Result<Boolean> updateDepartment(DepartmentVO departmentVO);

    /**
     * 根据ID删除对应的部门信息
     *
     * @return 是否删除成功
     */
    Result<Boolean> deleteDepartment();

    /**
     * 上传头像
     *
     * @param multipartFile 头像文件
     * @return 是否上传成功
     */
    Result<?> uploadHeadImage(MultipartFile multipartFile);

    /**
     * 查询头像
     *
     * @return 头像的url
     */
    Result<?> selectHeadImg();

    /**
     * 根据部门名称查询部门信息
     *
     * @param name 部门名称
     * @return 部门信息
     */
    Result<?> selectDepartmentByName(String name);
}
