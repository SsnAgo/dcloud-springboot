package com.example.dcloud.controller;


import com.example.dcloud.pojo.Department;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.service.IDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ssn
 * @since 2021-03-29
 */
@RestController
@RequestMapping("/school/department")
public class DepartmentController {
    @Resource
    private IDepartmentService departmentService;

    @ApiOperation("根据学院id获取该学院信息")
    @GetMapping("/{id}")
    public Department getDepartment(@PathVariable Integer id){
        return departmentService.getById(id);
    }

    @ApiOperation("新增学院")
    @PostMapping("/")
    public RespBean addDepartment(@RequestBody Department department){
        return departmentService.addDepartment(department);
    }

    @ApiOperation("修改学院")
    @PutMapping("/")
    public RespBean updateDepartment(@RequestBody Department department){
        return departmentService.updateDepartment(department);
    }

}
