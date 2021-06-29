package com.example.dcloud.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.pojo.Department;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.School;
import com.example.dcloud.service.ISchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Delete;
import org.springframework.transaction.annotation.Transactional;
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
    public RespBean deleteSchool(@PathVariable @ApiParam("学校id") Integer id) {
        return schoolService.deleteSchool(id);
    }

    @ApiOperation("修改学校信息")
    @PutMapping("/manage/")
    public RespBean updateSchool(@RequestBody School school){
        return schoolService.updateSchool(school);
    }

    @ApiOperation("新增学院，需要传上parentId")
    @PostMapping("/manage/dept")
    public RespBean addDept(@RequestBody School school){
        School exist = schoolService.getOne(new QueryWrapper<School>().eq("parentId", school.getParentId()).eq("name",school.getName()));
        if (exist != null) {
            return RespBean.error("该学校已有同名学院");
        }
        if (schoolService.save(school)) {
            return RespBean.success("新增学院成功");
        }
        return RespBean.error("新增学院失败");
    }

    @ApiOperation("修改学院")
    @PutMapping("/manage/dept")
    public RespBean updateDept(@RequestBody School school) {
        return schoolService.updateDept(school);
    }

    @ApiOperation("删除学院")
    @DeleteMapping("/manage/dept/{id}")
    public RespBean deleteDept(@PathVariable Integer id){
        if (schoolService.removeById(id)){
            return RespBean.success("删除学院成功");
        }
        return RespBean.error("删除学院失败");
    }
}
