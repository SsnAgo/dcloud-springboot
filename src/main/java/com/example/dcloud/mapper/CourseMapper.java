package com.example.dcloud.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud.pojo.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 分页获取课程信息
     * @param page
     * @param course
     * @return
     */
    IPage<Course> getCourses(Page<Course> page,@Param("course") Course course);

    /**
     * 分页按条件查询学生的班课
     *
     * @param sid
     * @param page
     * @param course
     * @return
     */
    IPage<Course> getStudentCourse(@Param("sid") Integer sid, Page<Course> page,@Param("course") Course course);

    IPage<Course> getTeacherCourse(@Param("tid")Integer tid, Page<Course> page,@Param("course") Course course);
}
