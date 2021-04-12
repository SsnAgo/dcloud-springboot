package com.example.dcloud.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.pojo.Course;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.RespPageBean;
import com.example.dcloud.pojo.User;
import com.example.dcloud.service.ICourseService;
import com.example.dcloud.service.ICourseStudentService;
import com.example.dcloud.utils.CourseUtils;
import com.example.dcloud.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
@Api(tags = "CourseController")
public class CourseController {

    @Resource
    private ICourseService courseService;
    @Resource
    private ICourseStudentService courseStudentService;

    @ApiOperation("获取所有课程（分页）")
    @GetMapping("/manage/")
    public RespPageBean getCourses(@RequestParam(defaultValue = "1") Integer currentPage,
                                   @RequestParam(defaultValue = "10") Integer size,
                                    Course course) {
        return courseService.getCourses(currentPage,size,course);
    }

    @ApiOperation("在管理系统新增班课")
    @PostMapping("/manage/")
    public RespBean manageAddCourse(@RequestBody Course course){
        String code = CourseUtils.generatorCourseCode();
        course.setCourseCode(code);
        course.setCreateTime(LocalDateTime.now());
        course.setCreaterId(UserUtils.getCurrentUser().getId());
        course.setPrcode(CourseUtils.PR_PREFIX+code);
        if (courseService.save(course)){
            return RespBean.success("新增班课成功",courseService.getById(course.getId()));
        }
        return RespBean.error("新增班课失败");
    }


    @ApiOperation("在管理系统修改班课")
    @PutMapping("/manage/")
    public RespBean manageUpdateCourse(@RequestBody Course course){
        if (courseService.updateById(course)){
            return RespBean.success("修改班课成功",course);
        }
        return RespBean.error("修改班课失败");
    }



    @ApiOperation("删除一个班课")
    @DeleteMapping("/manage/{id}")
    public RespBean deleteCourse(@PathVariable Integer cid){
        if (courseService.removeById(cid)) {
            return RespBean.success("删除班课成功");
        }
        return RespBean.error("删除班课失败");
    }


    @ApiOperation("批量删除班课")
    @DeleteMapping("/manage/")
    public RespBean deleteCourses(@RequestParam("ids") List list){
        return courseService.deleteCourses(list);
    }

    @ApiOperation("教师获取他创建的班课")
    @GetMapping("/mobile/teacher")
    public RespPageBean getTeacherCourse(@RequestParam(defaultValue = "1") Integer currentPage,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @ApiParam("按条件查询可传") Course course){
        User teacher = UserUtils.getCurrentUser();
        return courseService.getTeacherCourse(teacher.getId(),currentPage,size,course);
    }

    @ApiOperation("教师创建班课 如果是学生点 会返回无创建班课权利")
    @PostMapping("/mobile/teacher")
    public RespBean teacherAddCourse(@RequestBody Course course){
        if (UserUtils.getCurrentUser().getRoleId() != 2){
            return RespBean.error("无创建班课权利");
        }
        return courseService.teacherAddCourse(course);
    }

    @ApiOperation("学生获取所加入的所有班级列表（分页）")
    @GetMapping("/mobile/student")
    public RespPageBean getStudentCourse(@RequestParam(defaultValue = "1") Integer currentPage,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @ApiParam("按条件查询可传") Course course){
        User student = UserUtils.getCurrentUser();
        return courseService.getStudentCourse(student.getId(),currentPage,size,course);
    }

    @ApiOperation("学生根据班课号加入课堂")
    @PostMapping("/mobile/student")
    public RespBean studentAddCourse(@RequestParam("code") String code){
        User user = UserUtils.getCurrentUser();
        if (user.getRoleId() != 3) {
            return RespBean.error("请使用学生端加入班课");
        }
        return courseService.studentAddCourse(user.getId(),code);
    }

    @ApiOperation("教师查看班级成员")
    @GetMapping("/mobile/member")
    public RespPageBean courseMember(
            @RequestParam(required = true) Integer id,
            @RequestParam(defaultValue = "1") Integer currentPage,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @ApiParam("按学号或姓名模糊查询 可不传") String search,
                                     @ApiParam("按经验值或学号排序 exp or num")@RequestParam(defaultValue = "number") String sortBy){
        return courseService.courseMember(id,currentPage,size,search,sortBy);
    }

    @ApiOperation("根据id查看班课详情")
    @GetMapping("/manage/{id}")
    public RespBean getCourseInfo(@PathVariable Integer id){
        return courseService.getCourseInfo(id);
    }







}
