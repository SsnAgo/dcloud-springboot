package com.example.dcloud.service;

import com.example.dcloud.pojo.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dcloud.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ssn
 * @since 2021-03-29
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * 新增学院
     * @param department
     * @return
     */
    RespBean addDepartment(Department department);

    /**
     * 修改学院
     * @param department
     * @return
     */
    RespBean updateDepartment(Department department);
}
