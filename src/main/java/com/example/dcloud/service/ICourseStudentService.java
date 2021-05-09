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

    /**
     * 学生退出班课
     * @param studentId
     * @param courseId
     * @return
     */
    RespBean quitCourse(Integer studentId, Integer courseId);

    /**
     * 学生获取在该班课的经验值和排名
     * @param sid
     * @param cid
     * @return
     */
    RespBean studentGetExpRank(Integer sid, Integer cid);
}
