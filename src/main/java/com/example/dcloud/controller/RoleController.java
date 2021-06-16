package com.example.dcloud.controller;


import com.example.dcloud.dto.RidMidsDto;
import com.example.dcloud.pojo.Menu;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.Role;
import com.example.dcloud.service.IMenuRoleService;
import com.example.dcloud.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@RestController
@Api(tags = "RoleController")
@RequestMapping("/role/manage")
public class RoleController {

    @Resource
    private IMenuRoleService menuRoleService;

    @Resource
    private IRoleService roleService;

    @ApiOperation("获取角色,如果不传参就显示全部")
    @GetMapping("/")
    public List<Role> getRoles(@ApiParam("可按角色名查询") String search){
        return roleService.getRoles(search);
    }


    @ApiOperation("修改角色信息")
    @PutMapping("/")
    public RespBean editRole(@RequestBody Role role){
        if (roleService.updateById(role)){
            return RespBean.success("更新角色成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation("新增角色")
    @PostMapping("/")
    public RespBean addRole(@RequestBody Role role){
        if (!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_" + role.getName());
        }
        if (roleService.save(role)){
            return RespBean.success("新增角色成功");
        }
        return RespBean.error("新增角色失败");
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/{id}")
    public RespBean deleteRole(@PathVariable Integer id){
        if (roleService.removeById(id)){
            return RespBean.success("删除角色成功");
        }
        return RespBean.error("删除角色失败");
    }

    @ApiOperation("获取该角色的菜单（权限）")
    @GetMapping("/menus/{id}")
    public List<Menu> getMenusByRoleId(@PathVariable Integer id){
        return menuRoleService.getMenusByRoleId(id);
    }

    @ApiOperation("更新该角色的菜单权限")
    @PutMapping("/menus")
    public RespBean updateRoleMenus(@RequestBody RidMidsDto ridMidsDto){
        return menuRoleService.updateRoleMenus(ridMidsDto.getRid(),ridMidsDto.getIds());
    }

}
