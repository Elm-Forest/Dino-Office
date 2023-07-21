package com.dino.web.controller.department;

import com.dino.common.entity.Department;
import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.DepartmentVO;
import com.dino.department.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 企业管理
 *
 * @author 李嘉超
 * @author Zhang Jinming
 * @version 1.0
 */
@Api(tags = "企业管理")
@RestController
@RequestMapping(value = "/department")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    @ApiOperation(value = "查询企业信息")
    @GetMapping
    public Result<Department> selectDepartment() {
        return departmentService.selectDepartment();
    }

    @ApiOperation(value = "创建企业信息")
    @PostMapping("/create")
    public Result<?> insertDepartment(DepartmentVO departmentVO) {
        return departmentService.insertDepartment(departmentVO);
    }

    @ApiOperation(value = "删除企业信息")
    @DeleteMapping
    public Result<Boolean> deleteDepartment() {
        return departmentService.deleteDepartment();
    }

    @ApiOperation(value = "修改企业信息")
    @PutMapping
    public Result<Boolean> updateDepartment(DepartmentVO departmentVO) {
        return departmentService.updateDepartment(departmentVO);
    }

    @ApiOperation(value = "上传企业头像")
    @PutMapping("/img")
    public Result<?> uploadHeadImage(@RequestParam("file") MultipartFile file) {
        return departmentService.uploadHeadImage(file);
    }

    @ApiOperation(value = "查询企业头像")
    @GetMapping("/img")
    public Result<?> selectHeadImage() {
        return departmentService.selectHeadImg();
    }
}
