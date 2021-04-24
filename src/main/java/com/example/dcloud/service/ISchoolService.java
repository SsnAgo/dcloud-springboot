package com.example.dcloud.service;

import com.example.dcloud.pojo.Department;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.School;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface ISchoolService extends IService<School> {

    /**
     * 获取所有学校及其学院信息
     * @return
     */
    List<School> getSchools();

    /**
     * 新增一个学校
     * @param school
     * @return
     */
    RespBean addSchool(School school);

    /**
     * 根据id删除学校
     * @param id
     * @return
     */
    RespBean deleteSchool(Integer id);

    /**
     * 更新学校信息
     * @return
     */
    RespBean updateSchool(School school);

    /**
     * 更新学院信息
     * @param school
     * @return
     */
    RespBean updateDept(School school);
}
