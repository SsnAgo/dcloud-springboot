package com.example.dcloud.controller;


import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.Role;
import com.example.dcloud.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/role")
public class RoleController {


    @Resource
    private IRoleService roleService;

    @ApiOperation("获取全部角色")
    @GetMapping("/")
    public List<Role> getRoles(){
        return roleService.list();
    }

    @ApiOperation("修改角色信息")
    @PutMapping("/")
    public RespBean editRole(@RequestBody Role role){
        if (roleService.updateById(role)){
            return RespBean.success("更新角色成功");
        }
        return RespBean.error("更新失败");
    }


}
