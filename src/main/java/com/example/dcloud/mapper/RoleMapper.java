package com.example.dcloud.mapper;

import com.example.dcloud.pojo.Role;
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
public interface RoleMapper extends BaseMapper<Role> {


    /**
     * 如果传了role就按条件  不然就显示全部
     * @param role
     * @return
     */
    List<Role> getRoles(@Param("role") Role role);
}
