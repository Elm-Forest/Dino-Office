package com.ctgu.web.controller.department;

import com.ctgu.common.entity.Department;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.DepartmentVO;
import com.ctgu.department.service.DepartmentService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author 李嘉超
 * @author Zhang Jinming
 * @version 1.0
 * @date 2022/8/18 10:01
 */
@Api(tags = "企业管理")
@RestController
@RequestMapping(value = "/department")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    @GetMapping
    public Result<Department> selectDepartment() {
        return departmentService.selectDepartment();
    }

    @PostMapping("/create")
    public Result<?> insertDepartment(DepartmentVO departmentVO) {
        return departmentService.insertDepartment(departmentVO);
    }

    @DeleteMapping
    public Result<Boolean> deleteDepartment() {
        return departmentService.deleteDepartment();
    }

    @PutMapping
    public Result<Boolean> updateDepartment(DepartmentVO departmentVO) {
        return departmentService.updateDepartment(departmentVO);
    }

    @PutMapping("/img")
    public Result<?> uploadHeadImage(@RequestParam("file") MultipartFile file) {
        return departmentService.uploadHeadImage(file);
    }

    @GetMapping("/img")
    public Result<?> selectHeadImage() {
        return departmentService.selectHeadImg();
    }
}
