package com.example.dcloud.service.impl;

import com.example.dcloud.mapper.CourseMapper;
import com.example.dcloud.pojo.CourseStudent;
import com.example.dcloud.mapper.CourseStudentMapper;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.service.ICourseStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@Service
public class CourseStudentServiceImpl extends ServiceImpl<CourseStudentMapper, CourseStudent> implements ICourseStudentService {

    @Resource
    private CourseStudentMapper courseStudentMapper;

    @Override
    public List<Integer> listSidByCid(Integer cid) {
        return courseStudentMapper.listSidByCid(cid);
    }

    @Override
    public RespBean quitCourse(Integer studentId, Integer courseId) {
        if (courseStudentMapper.deleteCourseBySidAndCid(studentId, courseId)){
            return RespBean.success("退出班课成功");
        }
        return RespBean.error("退出班课失败");

    }

    @Override
    public RespBean studentGetExpRank(Integer sid, Integer cid) {
        Map<String,Object> res = new HashMap<>();

        return null;
    }
}
