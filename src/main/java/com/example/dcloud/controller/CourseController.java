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
import com.example.dcloud.vo.CourseMemberVo;
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
                                   @ApiParam("可按班课名或班课号搜索")String search) {
        return courseService.getCourses(currentPage,size,search);
    }

    @ApiOperation("在管理系统新增班课")
    @PostMapping("/manage/")
    public RespBean manageAddCourse(@RequestBody Course course){
        String code = CourseUtils.generatorCourseCode();
        // 重复了就再重新随机三次  如果三次还挂了 就直接找最大值然后+1
        Integer counter = 0;
        while(courseService.getOne(new QueryWrapper<Course>().eq("courseCode",code))!= null && counter < 3) {
            code = CourseUtils.generatorCourseCode();
            counter ++;
        }
        // 如果三次还是重复  那就只好取max了
        if (counter == 3) {
            code = String.valueOf(Integer.valueOf(courseService.getMaxCourseCode()) + 1);
        }
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
    public RespBean deleteCourse(@PathVariable Integer id){
//        if (courseService.removeById(id)) {
//            return RespBean.success("删除班课成功");
//        }
        return courseService.deleteCourse(id);
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
                                         String search){
        User teacher = UserUtils.getCurrentUser();
        return courseService.getTeacherCourse(teacher.getId(),currentPage,size,search);
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
                                         String search){
        User student = UserUtils.getCurrentUser();
        return courseService.getStudentCourse(student.getId(),currentPage,size,search);
    }

    @ApiOperation("根据班课号搜索班课信息")
    @GetMapping("/")
    public RespBean getCourseInfoByCode(@RequestParam("code") String code){
        return courseService.getCourseInfoByCode(code);
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

    @ApiOperation("查看班级成员")
    @GetMapping("/mobile/member")
    public List<CourseMemberVo> courseMember(
            @ApiParam("班课id")@RequestParam Integer id,
                                     @ApiParam("按学号或姓名模糊查询 可不传") String search,
                                     @ApiParam("按经验值或学号排序 exp or num")@RequestParam(defaultValue = "number") String sortBy){
        return courseService.courseMember(id,search,sortBy);
    }

    @ApiOperation("根据班课id查看班课详情")
    @GetMapping("/manage/{id}")
    public RespBean getCourseInfo(@PathVariable Integer id){
        return courseService.getCourseInfo(id);
    }

    @ApiOperation("学生退出班课")
    @DeleteMapping("/mobile/student/{id}")
    public RespBean quitCourse(@PathVariable @ApiParam("班课id") Integer id){
        User student = UserUtils.getCurrentUser();
        return courseStudentService.quitCourse(student.getId(),id);
    }

    @ApiOperation("学生在班课成员列表获取他的经验值和排名")
    @GetMapping("/mobile/rank")
    public RespBean studentGetExpRank(@RequestParam("cid")@ApiParam("班课id") Integer cid){
        User student = UserUtils.getCurrentUser();
        return courseStudentService.studentGetExpRank(student.getId(),cid);
    }
}
