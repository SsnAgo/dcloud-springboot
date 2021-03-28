package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.mapper.RoleMapper;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.Role;
import com.example.dcloud.pojo.User;
import com.example.dcloud.mapper.UserMapper;
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
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
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
    private UserDetailsService userDetailsService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;


    /**
     * 通过用户名登录
     * @param username
     * @param password
     * @param request
     * @return
     */
    @Override
    public RespBean loginByUsername(String username, String password, HttpServletRequest request) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username",username));
        if (null != user || !passwordEncoder.matches(password,user.getPassword())){
            return RespBean.error("用户名或密码输入错误");
        }
        if (!user.getEnabled()){
            return RespBean.error("账号被禁用，请联系管理员");
        }
        user.setRoles(getRoles(user.getId()));

        //否则更新成功  更新security登录对象
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        log.info("==================更新security登录对象成功==================");
        // 返回token和tokenHead
        String token = jwtTokenUtil.generatorToken(user);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);

        return RespBean.success("登陆成功", tokenMap);
    }

    /**
     * 通过用手机号登录
     * @param phone
     * @param code
     * @param code
     * @return
     */
    @Override
    public RespBean loginByPhone(String phone, String code, HttpServletRequest request) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone",phone));
        if (null == user) return RespBean.error("该手机号未注册，请先注册");
        if (!StringUtils.hasText(code)) return RespBean.error("验证码不能为空");
        if (!user.getEnabled()){
            return RespBean.error("账号被禁用，请联系管理员");
        }
        user.setRoles(getRoles(user.getId()));
        //否则更新成功  更新security登录对象
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        log.info("==================更新security登录对象成功==================");
        // 返回token和tokenHead
        String token = jwtTokenUtil.generatorToken(user);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);

        return RespBean.success("登陆成功", tokenMap);
    }

    @Override
    public User getUserByUsernameOrPhone(String usernameOrPhone) {
        // 通过用户名查询用户
        User userByUsername = userMapper.selectOne(new QueryWrapper<User>().eq("username",usernameOrPhone).eq("enabled",true));
        // 通过手机号码查询
        User userByPhone = userMapper.selectOne(new QueryWrapper<User>().eq("phone",usernameOrPhone).eq("enabled",true));
        if (userByUsername != null) return userByUsername;
        if (userByPhone != null) return userByPhone;

        return null;
    }

    @Override
    public List<Role> getRoles(Integer userId) {
        return roleMapper.getRoles(userId);
    }
}
