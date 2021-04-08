package com.example.dcloud.service;

import com.example.dcloud.pojo.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.RespPageBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface ICourseService extends IService<Course> {

    /**
     * 分页获取所有课程列表（course为空则搜素全部，否则按条件搜搜
     * @param currentPage
     * @param size
     * @param course
     * @return
     */
    RespPageBean getCourses(Integer currentPage, Integer size, Course course);

    /**
     * 批量删除班课
     * @param list
     * @return
     */
    RespBean deleteCourses(List list);

    /**
     * 教师添加班课
     * @param course
     * @return
     */
    RespBean teacherAddCourse(Course course);

    /**
     * 分页获取学生加入班课列表
     *
     * @param sid
     * @param currentPage
     * @param size
     * @param course
     * @return
     */
    RespPageBean getStudentCourse(Integer sid, Integer currentPage, Integer size, Course course);

    /**
     * 教师分页获取他创建的班课，也可按条件查询
     * @param id
     * @param currentPage
     * @param size
     * @param course
     * @return
     */
    RespPageBean getTeacherCourse(Integer id, Integer currentPage, Integer size, Course course);

    /**
     * 学生根据班课号加入班级
     * @param sid
     * @param code
     * @return
     */
    RespBean studentAddCourse(Integer sid, String code);
}
