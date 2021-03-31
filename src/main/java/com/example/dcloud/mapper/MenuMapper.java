package com.example.dcloud.mapper;

import com.example.dcloud.pojo.Menu;
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
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> getMenusWithRole();

    /**
     * 根据用户id获取菜单
     */
    List<Menu> getMenusByUserId(Integer userId);


    /**
     * 根据条件查询menus 如果为空就查全部
     * @param menu
     * @return
     */
    List<Menu> getMenus(@Param("menu") Menu menu);
}
