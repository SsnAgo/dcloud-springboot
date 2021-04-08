package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud.dto.CourseMemberDto;
import com.example.dcloud.mapper.CourseStudentMapper;
import com.example.dcloud.pojo.*;
import com.example.dcloud.mapper.CourseMapper;
import com.example.dcloud.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloud.utils.CourseUtils;
import com.example.dcloud.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.System;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Resource
    private CourseMapper courseMapper;
    @Resource
    private CourseStudentMapper courseStudentMapper;
    @Override
    public RespPageBean getCourses(Integer currentPage, Integer size, Course course) {
        Page<Course> page = new Page<>(currentPage,size);
        IPage<Course> coursePage = courseMapper.getCourses(page,course);
        RespPageBean res = new RespPageBean(coursePage.getTotal(),coursePage.getRecords());
        return res;
    }

    @Override
    @Transactional
    public RespBean deleteCourses(List list) {
        Integer res = courseMapper.deleteBatchIds(list);
        if (res > 0) {
            return RespBean.success("批量删除成功");
        }
        return RespBean.error("批量删除失败");
    }

    @Override
    public RespBean teacherAddCourse(Course course) {
        User currentUser = UserUtils.getCurrentUser();
        if (currentUser.getRoleId() != 2) {
            return RespBean.error("没有创建班课的权限");
        }
        course.setCreateTime(LocalDateTime.now());
        course.setCourseCode(CourseUtils.generatorCourseCode());
        course.setCreaterId(currentUser.getId());
        if (courseMapper.insert(course) == 1){
            return RespBean.success("创建班课成功",course);
        }
        return RespBean.error("创建班课失败");
    }

    @Override
    public RespPageBean getStudentCourse(Integer sid, Integer currentPage, Integer size, Course course) {
        Page<Course> page = new Page<>(currentPage,size);
        IPage<Course> iPage = courseMapper.getStudentCourse(sid,page,course);
        RespPageBean pageBean = new RespPageBean(iPage.getTotal(),iPage.getRecords());
        return pageBean;
    }

    @Override
    public RespPageBean getTeacherCourse(Integer tid, Integer currentPage, Integer size, Course course) {
        Page<Course> page = new Page<>(currentPage,size);
        IPage<Course> iPage = courseMapper.getTeacherCourse(tid,page,course);
        RespPageBean pageBean = new RespPageBean(iPage.getTotal(),iPage.getRecords());
        return pageBean;
    }

    @Override
    @Transactional
    public RespBean studentAddCourse(Integer sid, String code) {
        // 验证班课存在且可用
        Course exist = courseMapper.selectOne(new QueryWrapper<Course>().eq("courseCode", code).eq("enabled", true));
        if (null == exist) {
            return RespBean.error("该班课不存在");
        }
        // 如果存在，则检查该学生有没有这个课了 有的就不加了
        Integer res = courseStudentMapper.selectCount(new QueryWrapper<CourseStudent>().eq("cid", exist.getId()));
        if (res > 0 ){
            return RespBean.error("您已加入该班课");
        }
        // 如果不存在  那么就可以加入班课
        CourseStudent courseStudent = new CourseStudent();
        courseStudent.setCid(exist.getId());
        courseStudent.setSid(sid);
        if (courseStudentMapper.insert(courseStudent) == 1) {
            return RespBean.success("新增班课成功");
        }
        return RespBean.error("新增班课失败");
    }

    @Override
    public RespPageBean courseMember(Integer id, Integer currentPage, Integer size, String search, String sortBy) {
        Page<CourseMemberDto> page = new Page<>(currentPage,size);
        IPage<CourseMemberDto> iPage = courseMapper.courseMember(id,page,search,sortBy);
        System.out.println("执行到这里了");
        RespPageBean respPageBean = new RespPageBean(iPage.getTotal(),iPage.getRecords());
        System.out.println("这里挂了");
        return respPageBean;
    }


}
