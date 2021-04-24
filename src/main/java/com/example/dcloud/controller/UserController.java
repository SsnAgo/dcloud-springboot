package com.example.dcloud.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.RespPageBean;
import com.example.dcloud.pojo.User;
import com.example.dcloud.service.IUserService;
import com.example.dcloud.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@RestController
@RequestMapping("/user")
@Api(tags = "UserController")
public class UserController {

    @Value("${default.password}")
    private String defaultPass;
    @Resource
    private IUserService userService;

    @ApiOperation("获取所有用户(分页) 除了用户自己")
    @GetMapping("/manage/")
    public RespPageBean getAllEmployees(@RequestParam(defaultValue = "1") Integer currentPage,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @ApiParam("可按姓名或学工号搜索") String search) {
        return userService.getUsersByPage(currentPage, size, search);
    }

    @ApiOperation("管理员新增用户")
    @PostMapping("/manage/")
    public RespBean adminAddUser(@RequestBody User user){
        User exist = userService.getOne(new QueryWrapper<User>().eq("phone",user.getPhone()));
        if (null != exist) {
            return RespBean.error("该手机号码已被绑定");
        }
        // 如果没设置用户名  就随便给个用户名
        if (!StringUtils.hasText(user.getUsername())){
            user.setUsername(UserUtils.generateUsername());
        }else{
            exist = userService.getOne(new QueryWrapper<User>().eq("username",user.getUsername()));
            if (exist != null) {
                return RespBean.error("用户名已被注册");
            }
        }
        // 如果没设置密码，就设置默认密码
        if (!StringUtils.hasText(user.getPassword())){
            user.setPassword(new BCryptPasswordEncoder().encode(defaultPass));
        }else{
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        user.setEnabled(true);
        user.setCreateTime(LocalDateTime.now());
        if (userService.save(user)) {
            return RespBean.success("新增用户成功");
        }
        return RespBean.error("新增用户失败");
    }


    @ApiOperation("管理员修改用户")
    @PutMapping("/manage/")
    public RespBean adminUpdateUser(@RequestBody User user){
        if (userService.updateById(user)){
            return RespBean.success("修改用户成功");
        }
        return RespBean.error("修改用户失败");
    }

    @ApiOperation("管理员删除用户")
    @DeleteMapping("/manage/{id}")
    public RespBean adminUpdateUser(@PathVariable Integer id){
        if (userService.removeById(id)){
            return RespBean.success("删除用户成功");
        }
        return RespBean.error("删除用户失败");
    }











}
