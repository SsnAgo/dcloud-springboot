package com.example.dcloud.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

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
    List<MenuRole> getAllByRid(@Param("rid")Integer rid);


    /**
     * 根据rid获取菜单
     * @param rid
     * @return
     */
    List<Menu> getMenusByRoleId(Integer rid);

    /**
     * 给rid赋值该拥有的菜单ids
     * @param rid
     * @param ids
     * @return
     */
    Integer insertRecords(@Param("rid") Integer rid,@Param("mids") Integer[] ids);

    /**
     * 删除该id菜单的对应的权限赋予
     * @param id
     * @return
     */
    Integer deleteMenu(@Param("mid") Integer id);
}
