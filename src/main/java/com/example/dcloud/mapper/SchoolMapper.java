package com.example.dcloud.mapper;

import com.example.dcloud.pojo.School;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface SchoolMapper extends BaseMapper<School> {

    /**
     * 获取所有学校及其学院信息
     * @return
     */
    List<School> getSchools();
}
