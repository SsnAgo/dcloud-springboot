package com.example.dcloud.mapper;

import com.example.dcloud.pojo.Menu;
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
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> getMenusWithRole();

}
