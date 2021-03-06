package com.example.dcloud.mapper;

import com.example.dcloud.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
     * @param search
     * @return
     */
    List<Menu> getMenus(@Param("search") String search);

    /**
     * 修改菜单顺序
     * @param id
     * @param seq
     * @return
     */
    Integer updateMenuSequence(@Param("mid") Integer id,@Param("seq") Integer seq);
}
