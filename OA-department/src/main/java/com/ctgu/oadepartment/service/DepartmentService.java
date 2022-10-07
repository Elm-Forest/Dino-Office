package com.ctgu.oadepartment.service;

import com.ctgu.oacommon.entity.Department;
import com.ctgu.oacommon.vo.DepartmentVO;
import com.ctgu.oacommon.vo.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 李嘉超
 * @author zhang jinming
 * @version 1.0
 * @date 2022/8/16 15:45
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


    Result<?> uploadHeadImage(MultipartFile multipartFile);

    Result<?> selectHeadImg();
}
