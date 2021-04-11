package com.example.dcloud.controller;


import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.RespPageBean;
import com.example.dcloud.pojo.User;
import com.example.dcloud.service.IUserService;
import com.example.dcloud.utils.FastDFSUtils;
import com.example.dcloud.utils.UserUtils;
import com.example.dcloud.vo.ChangePasswordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

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
                                        User user) {
        return userService.getUsersByPage(currentPage, size, user);
    }

    @ApiOperation("管理员新增用户")
    @PostMapping("/manage/addByAdmin")
    public RespBean adminAddUser(@RequestBody User user){
        // 如果没设置用户名  就随便给个用户名
        if (!StringUtils.hasText(user.getUsername())){
            user.setUsername(UserUtils.generateUsername());
        }
        // 如果没设置密码，就设置默认密码
        if (!StringUtils.hasText(user.getPassword())){
            user.setPassword(new BCryptPasswordEncoder().encode(defaultPass));
        }else{
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }

        user.setCreateTime(LocalDateTime.now());
        if (userService.save(user)) {
            return RespBean.success("新增用户成功");
        }
        return RespBean.error("新增用户失败");
    }


    @ApiOperation("管理员修改用户")
    @PutMapping("/manage/editByAdmin")
    public RespBean adminUpdateUser(@RequestBody User user){
        if (userService.updateById(user)){
            return RespBean.success("修改用户成功");
        }
        return RespBean.error("修改用户失败");
    }









}
