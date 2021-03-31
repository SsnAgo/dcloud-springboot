package com.example.dcloud.mapper;

import com.example.dcloud.pojo.Menu;
import com.example.dcloud.pojo.MenuRole;
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
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    /**
     * 根据rid获取菜单
     * @param rid
     * @return
     */
    List<Menu> getMenusByRoleId(Integer rid);
}
