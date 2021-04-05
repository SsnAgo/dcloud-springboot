package com.example.dcloud.controller;


import com.example.dcloud.pojo.Course;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.RespPageBean;
import com.example.dcloud.service.ICourseService;
import com.example.dcloud.utils.CourseUtils;
import com.example.dcloud.utils.UserUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
@RequestMapping("/course")
public class CourseController {

    @Resource
    private ICourseService courseService;

    @ApiOperation("获取所有课程（分页）")
    @GetMapping("/manage")
    public RespPageBean getCourses(@RequestParam(defaultValue = "1") Integer currentPage,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   Course course) {
        return courseService.getCourses(currentPage,size,course);
    }

    @ApiOperation("在管理系统新增班课")
    @PostMapping("/manage")
    public RespBean manageAddCourse(@RequestBody Course course){
        course.setCourseCode(CourseUtils.generatorCourseNumber());
        course.setCreateTime(LocalDateTime.now());
        course.setCreaterId(UserUtils.getCurrentUser().getId());
        if (courseService.save(course)){
            return RespBean.success("新增班课成功",course);
        }
        return RespBean.error("新增班课失败");
    }


    @ApiOperation("在管理系统修改班课")
    @PutMapping("/manage")
    public RespBean manageUpdateCourse(@RequestBody Course course){
        if (courseService.updateById(course)){
            return RespBean.success("修改班课成功",course);
        }
        return RespBean.error("更新班课失败");
    }



    @ApiOperation("删除一个班课")
    @DeleteMapping("/{id}")
    public RespBean deleteCourse(@PathVariable Integer cid){
        if (courseService.removeById(cid)) {
            return RespBean.success("删除班课成功");
        }
        return RespBean.error("删除班课失败");
    }


    @ApiOperation("批量删除班课")
    @DeleteMapping("/manage")
    public RespBean deleteCourses(@RequestParam("ids") List list){
        return courseService.deleteCourses(list);
    }





}
