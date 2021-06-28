package com.example.dcloud.service;

import com.example.dcloud.pojo.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.RespPageBean;
import com.example.dcloud.vo.CourseMemberVo;

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
     * @param search
     * @return
     */
    RespPageBean getCourses(Integer currentPage, Integer size, String search);

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
     * @param search
     * @return
     */
    RespPageBean getStudentCourse(Integer sid, Integer currentPage, Integer size, String search);

    /**
     * 教师分页获取他创建的班课，也可按条件查询
     * @param id
     * @param currentPage
     * @param size
     * @param search
     * @return
     */
    RespPageBean getTeacherCourse(Integer id, Integer currentPage, Integer size, String search);

    /**
     * 学生根据班课号加入班级
     * @param sid
     * @param code
     * @return
     */
    RespBean studentAddCourse(Integer sid, String code);

    /**
     * 获取班级全部成员
     *
     * @param id
     * @param search
     * @param sortBy
     * @return
     */
    List<CourseMemberVo> courseMember(Integer id, String search, String sortBy);

    /**
     * 根据id获取班课详情
     * @param id
     * @return
     */
    RespBean getCourseInfo(Integer id);

    /**
     * 根据班课号查看班课信息
     * @param code
     * @return
     */
    RespBean getCourseInfoByCode(String code);

    /**
     * 获取数据库里最大的
     * @return
     */
    String getMaxCourseCode();

    /**
     * 删除班课
     * @param id
     * @return
     */
    RespBean deleteCourse(Integer id);
}
