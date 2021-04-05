package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud.pojo.Course;
import com.example.dcloud.mapper.CourseMapper;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.RespPageBean;
import com.example.dcloud.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Resource
    private CourseMapper courseMapper;
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
}
