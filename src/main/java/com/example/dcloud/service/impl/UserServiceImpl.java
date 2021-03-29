package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.mapper.*;
import com.example.dcloud.pojo.*;
import com.example.dcloud.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloud.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.System;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private MajorMapper majorMapper;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private SchoolMapper schoolMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private DictInfoMapper dictInfoMapper;

    @Value("${jwt.tokenHead}")
    private String tokenHead;


    /**
     * 通过用户名登录
     *
     * @param username
     * @param password
     * @param request
     * @return
     */
    @Override
    public RespBean loginByUsername(String username, String password, HttpServletRequest request) {
        //UserDetails user = userDetailsService.loadUserByUsername(username);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        System.out.println(passwordEncoder.matches(password, user.getPassword()));
        if (null == user || !passwordEncoder.matches(password, user.getPassword())) {
            return RespBean.error("用户名或密码输入错误");
        }
        return loginSuccess(user);
    }

    /**
     * 如果id为空就查全部  如果id不为空就查所有
     * @param user
     * @return
     */
    @Override
    public User getUserInfo(User user) {
        user = user.setSex(getSex(user));
    user = user.setEducation(getEducation(user));
    user = user.setSchool(getSchool(user));
    user = user.setDepartment(getDepartment(user));
    user = user.setMajor(getMajor(user));
        return user;
    }

    /**
     * 通过用手机号登录
     *
     * @param phone
     * @param code
     * @param code
     * @return
     */
    @Override
    public RespBean loginByPhone(String phone, String code, HttpServletRequest request) {
        //UserDetails user = userDetailsService.loadUserByUsername(phone);
        User user  = userMapper.selectOne(new QueryWrapper<User>().eq("phone",phone));
        if (null == user) return RespBean.error("该手机号未注册，请先注册");
        if (!StringUtils.hasText(code)) return RespBean.error("验证码不能为空");
        return loginSuccess(user);
    }

    private RespBean loginSuccess(User user){
        if (!user.isEnabled()) {
            return RespBean.error("账号被禁用，请联系管理员");
        }
        user.setRoles(roleMapper.getRoles(user.getId()));
        user = getUserInfo(user);
        System.out.println("存入前：" + user);
        //否则更新成功  更新security登录对象
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        log.info("==================更新security登录对象成功==================");
        // 返回token和tokenHead
        String token = jwtTokenUtil.generatorToken(user);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        User user2 = (User)usernamePasswordAuthenticationToken.getPrincipal();
        System.out.println("从usernamepasswordAu里面取user打印" + user2);
        return RespBean.success("登陆成功", tokenMap);
    }

    @Override
    @Transactional
    public User getUserByUsernameOrPhone(String usernameOrPhone) {
        // 通过用户名查询用户
        User userByUsername = userMapper.selectOne(new QueryWrapper<User>().eq("username", usernameOrPhone));
        // 通过手机号码查询
        User userByPhone = userMapper.selectOne(new QueryWrapper<User>().eq("phone", usernameOrPhone));
        if (userByUsername != null) return userByUsername;
        if (userByPhone != null) return userByPhone;
        return null;
    }

    @Override
    public User getCompletedUser(User user) {
        //user.setSex(getSex(user)).setEducation(getEducation(user)).setSchool();
        return null;
    }


    @Override
    public List<Role> getRoles(Integer userId) {
        return roleMapper.getRoles(userId);
    }

    @Override
    public String getSex(User user) {
        // 根据user的sexCode来获取性别内容
        System.out.println(user.getSexCode());
        String sex = dictInfoMapper.selectById(user.getSexCode()).getContent();
        log.info("获取到用户性别为：{}", sex);
        return sex;
    }

    @Override
    public String getEducation(User user) {
        // 根据user的educationCode来获取性别内容
        String education = dictInfoMapper.selectById(user.getEducationCode()).getContent();
        log.info("获取到用户学历为：{}", education);
        return education;
    }

    @Override
    public School getSchool(User user) {

        return schoolMapper.selectById(user.getSchoolId());
    }

    @Override
    public Department getDepartment(User user) {

        return departmentMapper.selectById(user.getDepartmentId());
    }


    @Override
    public Major getMajor(User user) {
        Major major = majorMapper.selectById(user.getMajorId());
        log.info("获取到用户学历为：{}", major.getName());
        return major;
    }
}
