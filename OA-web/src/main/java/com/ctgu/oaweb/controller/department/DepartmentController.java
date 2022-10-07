package com.ctgu.oaweb.controller.department;

import com.ctgu.oacommon.entity.Department;
import com.ctgu.oacommon.vo.DepartmentVO;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oadepartment.service.DepartmentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 李嘉超
 * @author Zhang Jinming
 * @version 1.0
 * @date 2022/8/18 10:01
 */
@RestController
@RequestMapping(value = "/department")
@Api(tags = "企业管理")
public class DepartmentController {

    @Autowired
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
