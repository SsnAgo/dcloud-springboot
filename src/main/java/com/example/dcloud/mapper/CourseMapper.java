package com.example.dcloud.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud.vo.CourseMemberVo;
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
     * @param search
     * @return
     */
    IPage<Course> getCourses(Page<Course> page,@Param("search") String search);

    /**
     * 分页按条件查询学生的班课
     *
     * @param sid
     * @param page
     * @param search
     * @return
     */
    IPage<Course> getStudentCourse(@Param("sid") Integer sid, Page<Course> page,@Param("search") String search);

    IPage<Course> getTeacherCourse(@Param("tid")Integer tid, Page<Course> page,@Param("search") String search);

    /**
     * 分页 按条件查询并排序 该班级的成员
     * @param id
     * @param page
     * @param search
     * @param sortBy
     * @return
     */
    IPage<CourseMemberVo> courseMember(@Param("cid") Integer id, Page<CourseMemberVo> page, @Param("search") String search, @Param("sortBy") String sortBy);

    /**
     * 根据id获取课程
     * @param id
     * @return
     */
    Course getCourseInfo(@Param("id") Integer id);
}
