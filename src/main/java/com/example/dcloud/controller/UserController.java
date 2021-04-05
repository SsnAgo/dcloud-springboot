package com.example.dcloud.controller;


import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.RespPageBean;
import com.example.dcloud.pojo.User;
import com.example.dcloud.service.IUserService;
import com.example.dcloud.utils.FastDFSUtils;
import com.example.dcloud.utils.UserUtils;
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

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/info")
    public User userInfo(Principal principal){
        if (null == principal){
            return null;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        User user = (User) usernamePasswordAuthenticationToken.getPrincipal();
        user = userService.getUserInfo(user);
        System.out.println("controller里的user : " + user);
        user.setPassword(null);
        return user;
    }

    @ApiOperation("获取所有用户(分页) 除了用户自己")
    @GetMapping("/manage")
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

    @ApiOperation("修改个人信息")
    @PutMapping("/editSelfInfo")
    public RespBean updateSelf(@RequestBody User user, Authentication authentication){
        if (userService.updateById(user)){
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user,null,authentication.getAuthorities()));
            return RespBean.success("更新成功",user);
        }
        return RespBean.error("更新失败");
    }


    @ApiOperation("更新用户头像")
    @PostMapping("/userfase")
    public RespBean updateAdminUserFace(MultipartFile file, Integer id, Authentication authentication){
        String[] fileResult = FastDFSUtils.uploadFile(file);
        String url = FastDFSUtils.getTrackerUrl() + fileResult[0] + "/" + fileResult[1];
        return userService.updateUserFace(url,id,authentication);
    }

    @ApiOperation("修改密码")
    @PostMapping("/changepwd")
    public RespBean changePassword(@RequestParam Integer id,@RequestParam String oldPassword,@RequestParam String newPassword){
        return userService.changePassword(id,oldPassword,newPassword);
    }







}
