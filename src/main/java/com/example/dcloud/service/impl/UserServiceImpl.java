package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud.mapper.*;
import com.example.dcloud.pojo.*;
import com.example.dcloud.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloud.utils.JwtTokenUtil;
import com.example.dcloud.utils.SmsUtils;
import com.example.dcloud.utils.UserUtils;
import com.example.dcloud.dto.ChangePasswordDto;
import com.example.dcloud.dto.RegisterDto;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.System;
import java.util.HashMap;
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

    @Resource
    private SmsUtils smsUtils;

    @Value("${jwt.tokenHead}")
    private String tokenHead;



    /**
     * 管理端通过用户名登录
     *
     * @param usernameOrPhone
     * @param password
     * @param request
     * @return
     */
    @Override
    public RespBean manageLoginByPassword(String usernameOrPhone, String password, HttpServletRequest request) {
        //UserDetails user = userDetailsService.loadUserByUsername(username);
        User user = getUserByUsernameOrPhone(usernameOrPhone);
        if (null == user || !passwordEncoder.matches(password, user.getPassword())) {
            return RespBean.error("用户名/手机号或密码输入错误");
        }
        if (user.getRoleId() != 1 && user.getRoleId() != 2){
            return RespBean.error("没有进入管理系统的权限");
        }
        return loginSuccess(user);
    }

    /**
     * 补全user属性
     *
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
        user = user.setRole(getRole(user));
        return user;
    }

    /**
     * 管理端通过用手机号登录
     *
     * @param phone
     * @param code
     * @param code
     * @return
     */
    @Override
    public RespBean manageLoginByCode(String phone, String code, HttpServletRequest request) {
//        UserDetails user = userDetailsService.loadUserByUsername(phone);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", phone));
        System.out.println("user信息为"+user);
        if (null == user) return RespBean.error("该手机号未注册，请先注册");
        if (!StringUtils.hasText(code)) return RespBean.error("验证码不能为空");
        Integer res = smsUtils.validateCode(phone, code);
        if (res == SmsUtils.NO_PHONE){
            return RespBean.error("请先获取验证码");
        }
        if (res == SmsUtils.CODE_ERROR){
            return RespBean.error("验证码输入错误");
        }
        return loginSuccess(user);
    }

    /**
     * 手机端通过手机号/账号 + 密码登录
     * @param usernameOrPhone
     * @param password
     * @param request
     * @return
     */
    @Override
    public RespBean mobileLoginByPassword(String usernameOrPhone, String password, HttpServletRequest request) {
        User user = getUserByUsernameOrPhone(usernameOrPhone);
        if (null == user || !passwordEncoder.matches(password, user.getPassword())) {
            return RespBean.error("用户名/手机号或密码输入错误");
        }
        if (user.getRoleId() != 2 && user.getRoleId() != 3){
            return RespBean.error("手机端仅允许教师/学生登录");
        }
        return loginSuccess(user);
    }

    /**
     * 手机端通过手机号验证码登录
     * @param phone
     * @param code
     * @param request
     * @return
     */
    @Override
    public RespBean mobileLoginByCode(String phone, String code, HttpServletRequest request) {
        //UserDetails user = userDetailsService.loadUserByUsername(phone);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", phone));
        if (null == user) return RespBean.error("该手机号未注册，请先注册");
        if (!StringUtils.hasText(code)) return RespBean.error("验证码不能为空");
        if (user.getRoleId() != 2 && user.getRoleId() != 3){
            return RespBean.error("没有进入管理系统的权限");
        }
        Integer res = smsUtils.validateCode(phone, code);
        if (res == SmsUtils.NO_PHONE){
            return RespBean.error("请先获取验证码");
        }
        if (res == SmsUtils.CODE_ERROR){
            return RespBean.error("验证码输入错误");
        }
        System.out.println(res);
        return loginSuccess(user);
    }

    private RespBean loginSuccess(User user) {
        if (!user.isEnabled()) {
            return RespBean.error("账号被禁用，请联系管理员");
        }
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
        User user2 = (User) usernamePasswordAuthenticationToken.getPrincipal();
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
    public String getSex(User user) {
        // 根据user的sexCode来获取性别内容
        System.out.println(user.getSexCode());
        DictInfo sexinfo = dictInfoMapper.selectById(user.getSexCode());
        if (sexinfo == null) return "";
        String sex = sexinfo.getContent();
        log.info("获取到用户性别为：{}", sex);
        return sex;
    }

    @Override
    public String getEducation(User user) {
        // 根据user的educationCode来获取性别内容
        DictInfo eduinfo = dictInfoMapper.selectById(user.getEducationCode());
        if (eduinfo == null) return "";
        String education = eduinfo.getContent();
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
        log.info("获取到用户专业：{}", major);
        return major;
    }

    @Override
    public RespPageBean getUsersByPage(Integer currentPage, Integer size, String search) {

        Page<User> page = new Page<>(currentPage, size);
        IPage<User> usersPage = userMapper.getUsersByPage(UserUtils.getCurrentUser().getId(), page, search);
        RespPageBean respPageBean = new RespPageBean(usersPage.getTotal(), usersPage.getRecords());
        return respPageBean;
    }

    @Override
    public Role getRole(User user) {
        Role role = roleMapper.selectById(user.getRoleId());
        log.info("获取到用户角色：{}", role);
        return role;
    }

    @Override
    @Transactional
    public RespBean updateUserFace(String url,User user) {
        user.setUserFace(url);
        if (userMapper.updateById(user) == 1) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
            return RespBean.success("更新头像成功", url);
        }
        return RespBean.error("更新头像失败");
    }

    @Override
    @Transactional
    public RespBean changePassword(ChangePasswordDto vo) {
        User user = userMapper.selectById(vo.getId());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(vo.getOldPassword(),user.getPassword())){
            return RespBean.error("当前密码输入不正确，请重新输入");
        }
        //验证通过了就修改密码
        user.setPassword(bCryptPasswordEncoder.encode(vo.getNewPassword()));
        if (userMapper.updateById(user) == 1) {
            return RespBean.success("修改密码成功");
        }
        return RespBean.error("修改密码失败");
    }

    @Override
    @Transactional
    public RespBean getLoginCaptcha(String phone) {

        // 登录的话，先认证手机号是否存在
        User exist = userMapper.selectOne(new QueryWrapper<User>().eq("phone", phone));
        if (null == exist){
            return RespBean.error("该手机号未注册，请先注册");
        }
        if (!exist.isEnabled()){
            return RespBean.error("该账号已被禁用，请联系管理员");
        }
        // 手机号可用，前往获取验证码
        return sendSms(phone);
    }

    @Override
    @Transactional
    public RespBean getRegisterCaptcha(String phone) {

        // 注册的话，先认证手机号是否 不存在
        User exist = userMapper.selectOne(new QueryWrapper<User>().eq("phone", phone.trim()));
        if (null != exist){
            return RespBean.error("该手机号已注册，请去登录");
        }
        return sendSms(phone);
    }

    @Override
    @Transactional
    public RespBean register(RegisterDto registerDto) {
        User exist = userMapper.selectOne(new QueryWrapper<User>().eq("username", registerDto.getUsername()));
        if (exist != null) {
            return RespBean.error("该用户名已被注册");
        }
        Integer res = smsUtils.validateCode(registerDto.getPhone(), registerDto.getCode());
        if (res == SmsUtils.NO_PHONE){
            return RespBean.error("请先获取验证码");
        }
        if (res == SmsUtils.CODE_ERROR){
            return RespBean.error("验证码输入错误");
        }
        User user = new User();
        user.setEnabled(true);
        user.setRoleId(registerDto.getRoleId());
        user.setUsername(registerDto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(registerDto.getPassword()));
        user.setPhone(registerDto.getPhone());
        if (userMapper.insert(user) == 1){
            return RespBean.success("注册成功");
        }
        return RespBean.success("注册失败");
    }


    public RespBean sendSms(String phone){
        // 手机号可用，前往获取验证码
        SendSmsResponse resp = smsUtils.SendSms(phone);
        if (resp == null) {
            return RespBean.error("发送验证码遇到错误，请重试");
        }
        if (!resp.getSendStatusSet()[0].getCode().equals("Ok")){
            return RespBean.error("发送验证码遇到错误，请重试");
        }
        return RespBean.success("发送验证码成功");
    }
}
