package com.example.dcloud.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.example.dcloud.pojo.MenuRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {
    List<MenuRole> getAllByRid(@Param("rid")Integer rid);


}
