package com.example.dcloud.service;

import com.example.dcloud.pojo.CourseStudent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dcloud.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface ICourseStudentService extends IService<CourseStudent> {

    /**
     * 获取该班课所有学生id列表
     * @param cid
     * @return
     */
    List<Integer> listSidByCid(Integer cid);
}
