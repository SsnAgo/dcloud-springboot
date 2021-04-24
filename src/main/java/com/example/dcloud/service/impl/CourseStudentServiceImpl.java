package com.example.dcloud.service.impl;

import com.example.dcloud.mapper.CourseMapper;
import com.example.dcloud.pojo.CourseStudent;
import com.example.dcloud.mapper.CourseStudentMapper;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.service.ICourseStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class CourseStudentServiceImpl extends ServiceImpl<CourseStudentMapper, CourseStudent> implements ICourseStudentService {

    @Resource
    private CourseStudentMapper courseStudentMapper;
    @Override
    public List<Integer> listSidByCid(Integer cid) {
        return courseStudentMapper.listSidByCid(cid);
    }
}
