package com.example.dcloud.mapper;

import com.example.dcloud.pojo.Role;
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
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 获取该user的roles
     * @return
     */
    List<Role> getRoles(Integer userId);

}
