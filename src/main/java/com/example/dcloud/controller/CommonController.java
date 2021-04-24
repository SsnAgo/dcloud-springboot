package com.example.dcloud.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.pojo.*;
import com.example.dcloud.service.*;
import com.example.dcloud.utils.FastDFSUtils;
import com.example.dcloud.utils.UserUtils;
import com.example.dcloud.dto.ChangePasswordDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.lang.System;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/common")
@Api(tags = "CommonController")
public class CommonController {

    @Resource
    private IDictInfoService dictInfoService;
    @Resource
    private ISchoolService schoolService;
    //    @Resource
//    private IDepartmentService departmentService;
    @Resource
    private IMajorService majorService;
    @Resource
    private IUserService userService;

    @ApiOperation("根据tag获取该字典的字典项信息")
    @GetMapping("/dict/info/{tag}")
    public List<DictInfo> getDictInfoByTag(@PathVariable String tag) {
        return dictInfoService.list(new QueryWrapper<DictInfo>().eq("tag", tag));
    }

    @ApiOperation("获取所有学校列表")
    @GetMapping("/school")
    public List<School> getSchools() {
        return schoolService.list(new QueryWrapper<School>().eq("parentId", -1));
    }

    @ApiOperation("获取该学校id的所有学院列表")
    @GetMapping("/school/{id}/departments")
    public List<School> getDepartments(@PathVariable Integer id) {
        return schoolService.list(new QueryWrapper<School>().eq("parentId", id));
    }

    @ApiOperation("获取所有学校及其分院(树状结构)")
    @GetMapping("/school/tree")
    public List<School> getSchoolAndDepts() {
        return schoolService.getSchools();
    }

    @ApiOperation("获取所有专业列表")
    @GetMapping("/major")
    public List<Major> getMajors() {
        return majorService.list();
    }


    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/user/info")
    public User userInfo(Principal principal) {
        User user = UserUtils.getCurrentUser();
//        user = userService.getUserInfo(user);
        System.out.println("controller里的user : " + user);
        user.setPassword(null);
        return user;
    }

    @ApiOperation("修改个人信息")
    @PutMapping("/user/edit")
    public RespBean updateSelf(@RequestBody User user) {
        User origin = userService.getById(user.getId());
        if (user.getUsername() != null && !origin.getUsername().equals(user.getUsername())) {
            return RespBean.error("用户名不能修改");
        }
        if (user.getPhone() != null && !origin.getPhone().equals(user.getPhone())) {
            User exist = userService.getOne(new QueryWrapper<User>().eq("phone", user.getPhone()));
            if (null != exist) {
                return RespBean.error("该手机号已被绑定");
            }
        }
        if (userService.updateById(user)) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
            return RespBean.success("更新成功", user);
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation("更新用户头像")
    @PostMapping("/user/face")
    public RespBean updateAdminUserFace(MultipartFile file) {
        User user = UserUtils.getCurrentUser();
        String[] fileResult = FastDFSUtils.uploadFile(file);
        String url = FastDFSUtils.getTrackerUrl() + fileResult[0] + "/" + fileResult[1];
        return userService.updateUserFace(url, user);
    }

    @ApiOperation("修改密码")
    @PostMapping("/user/pwd")
    public RespBean changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        return userService.changePassword(changePasswordDto);
    }

    @ApiOperation("上传图片到fdfs并获取该url")
    @PostMapping("/image")
    public String uploadImage(MultipartFile file) {
        String[] fileResult = FastDFSUtils.uploadFile(file);
        String url = FastDFSUtils.getTrackerUrl() + fileResult[0] + "/" + fileResult[1];
        return url;
    }
}
