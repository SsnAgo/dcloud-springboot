package com.example.dcloud.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud.pojo.Course;
import com.example.dcloud.pojo.CourseStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface CourseStudentMapper extends BaseMapper<CourseStudent> {

    /**
     * 根据cid获取该班级的所有sid
     * @param cid
     * @return
     */
    List<Integer> listSidByCid(Integer cid);
}
