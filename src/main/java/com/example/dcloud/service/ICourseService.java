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
}
