package com.example.dcloud.controller;


import com.example.dcloud.pojo.Department;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.School;
import com.example.dcloud.service.ISchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@RestController
@Api(tags = "SchoolController")
@RequestMapping("/school")
public class SchoolController {

    @Resource
    private ISchoolService schoolService;

    @ApiOperation("获取所有学校及其分院(树状结构)")
    @GetMapping("/manage/tree")
    public List<School> getSchoolAndDepts() {
        return schoolService.getSchools();
    }

    @ApiOperation("新增学校")
    @PostMapping("/manage/")
    public RespBean addSchool(@RequestBody School school) {
        return schoolService.addSchool(school);
    }

    @ApiOperation("删除学校")
    @DeleteMapping("/manage/{id}")
    public RespBean deleteSchool(@PathVariable @ApiParam("学校id") Integer sid) {
        return schoolService.deleteSchool(sid);
    }

    @ApiOperation("修改学校信息")
    @PutMapping("/")
    public RespBean updateSchool(@RequestBody School school){
        return schoolService.updateSchool(school);
    }

//    @ApiOperation("获取所有学校信息")
//    @GetMapping("/")
//    public List<School> getSchools(){
//        return schoolService.list();
//    }



    
}
