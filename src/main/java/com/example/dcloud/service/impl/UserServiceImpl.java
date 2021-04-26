package com.example.dcloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud.mapper.*;
import com.example.dcloud.pojo.*;
import com.example.dcloud.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloud.utils.*;
import com.example.dcloud.dto.ChangePasswordDto;
import com.example.dcloud.dto.RegisterDto;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
import java.io.*;
import java.lang.System;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

//    @Resource
//    private DepartmentMapper departmentMapper;

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

    @Value("${default.password}")
    private String defaultPassword;

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
        if (user.getRoleId() != 1 && user.getRoleId() != 2) {
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
        System.out.println("user信息为" + user);
        if (null == user) return RespBean.error("该手机号未注册，请先注册");
        if (!StringUtils.hasText(code)) return RespBean.error("验证码不能为空");
        Integer res = smsUtils.validateCode(phone, code);
        if (res == SmsUtils.NO_PHONE) {
            return RespBean.error("请先获取验证码");
        }
        if (res == SmsUtils.CODE_ERROR) {
            return RespBean.error("验证码输入错误");
        }
        return loginSuccess(user);
    }

    /**
     * 手机端通过手机号/账号 + 密码登录
     *
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
        if (user.getRoleId() != 2 && user.getRoleId() != 3) {
            return RespBean.error("手机端仅允许教师/学生登录");
        }
        return loginSuccess(user);
    }

    /**
     * 手机端通过手机号验证码登录
     *
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
        if (user.getRoleId() != 2 && user.getRoleId() != 3) {
            return RespBean.error("没有进入管理系统的权限");
        }
        Integer res = smsUtils.validateCode(phone, code);
        if (res == SmsUtils.NO_PHONE) {
            return RespBean.error("请先获取验证码");
        }
        if (res == SmsUtils.CODE_ERROR) {
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
    public School getDepartment(User user) {
        return schoolMapper.selectById(user.getDepartmentId());
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
    public RespBean updateUserFace(String url, User user) {
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
        if (!bCryptPasswordEncoder.matches(vo.getOldPassword(), user.getPassword())) {
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
        if (null == exist) {
            return RespBean.error("该手机号未注册，请先注册");
        }
        if (!exist.isEnabled()) {
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
        if (null != exist) {
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
        if (res == SmsUtils.NO_PHONE) {
            return RespBean.error("请先获取验证码");
        }
        if (res == SmsUtils.CODE_ERROR) {
            return RespBean.error("验证码输入错误");
        }
        User user = new User();
        user.setEnabled(true);
        user.setRoleId(registerDto.getRoleId());
        user.setUsername(registerDto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(registerDto.getPassword()));
        user.setPhone(registerDto.getPhone());
        if (userMapper.insert(user) == 1) {

            return RespBean.success("注册成功");
        }
        return RespBean.success("注册失败");
    }

    @Override
    public RespBean oauthLogin(String clientId, String clientSecret, String code, HttpServletRequest req) throws Exception {
//        if (StringUtils.hasText(code)) {
//            //拿到我们的code,去请求token
//            //发送一个请求到
//            System.out.println("code is " + code);
//            String token_url = GitHubConstant.TOKEN_URL.replace("CODE", code);
//            //得到的responseStr是一个字符串需要将它解析放到map中
//            String responseStr = HttpClientUtils.doGet(token_url);
//            // 调用方法从map中获得返回的--》 令牌
//            String token = HttpClientUtils.getMap(responseStr).get("access_token");
//            System.out.println("token is :" +  token);
//            //根据token发送请求获取登录人的信息  ，通过令牌去获得用户信息
//            String userinfo_url = GitHubConstant.USER_INFO_URL.replace("TOKEN", token);
//            responseStr = HttpClientUtils.doGet(userinfo_url);//json
//            System.out.println("resp str : " + responseStr);
//            Map<String, String> responseMap = HttpClientUtils.getMapByJson(responseStr);
//
//            // 成功则登陆
//            System.out.println("成功");
//        }
//        return RespBean.error("失败");
        try{
            String token_url = sendPost("https://github.com/login/oauth/access_token?client_id="+clientId+"&client_secret="+clientSecret+"&code="+code,null);
            String token = token_url.split("&")[0];
            String token1 = token.substring(13);
            // String res = httpGet("https://api.github.com/user?" + token + "", "token  " + token);
            String res = httpGet("https://api.github.com/user", "token  " + token1);
            JSONObject user = (JSONObject) JSON.parse(res);
            return RespBean.success("成功",user);
        }catch (Exception e){
            e.printStackTrace();
            return RespBean.error("失败");
        }

    }

    @Override
    public RespBean quickRegister(RegisterDto registerDto) {
        String phone = registerDto.getPhone();
        String code = registerDto.getCode();
        Integer roleId = registerDto.getRoleId();
        Integer res = smsUtils.validateCode(phone, code);
        if (res == SmsUtils.NO_PHONE) {
            return RespBean.error("请先获取验证码");
        }
        if (res == SmsUtils.CODE_ERROR) {
            return RespBean.error("验证码输入错误");
        }
        User user = new User();
        user.setEnabled(true);
        user.setRoleId(roleId);
        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(new BCryptPasswordEncoder().encode(defaultPassword));
        user.setPhone(phone);
        if (userMapper.insert(user) == 1) {

            return RespBean.success("注册成功");
        }
        return RespBean.success("注册失败");
    }


    public RespBean sendSms(String phone) {
        // 手机号可用，前往获取验证码
        SendSmsResponse resp = smsUtils.SendSms(phone);
        if (resp == null) {
            return RespBean.error("发送验证码遇到错误，请重试");
        }
        if (!resp.getSendStatusSet()[0].getCode().equals("Ok")) {
            return RespBean.error("发送验证码遇到错误，错误信息 : " + resp.getSendStatusSet()[0].getMessage());
        }
        return RespBean.success("发送验证码成功");
    }


    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            InputStream instream = conn.getInputStream();
            if(instream!=null){
                in = new BufferedReader( new InputStreamReader(instream));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    public static String httpGet(String url, String token){
        System.out.println(token);
        // 获取连接客户端工具
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse=null;
        String finalString = null;
        HttpGet httpGet = new HttpGet(url);
        /**公共参数添加至httpGet*/

        /*header中通用属性*/
        httpGet.setHeader("Accept","*/*");
        httpGet.setHeader("Accept-Encoding","gzip, deflate");
        httpGet.setHeader("Cache-Control","no-cache");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
        /*业务参数*/
        httpGet.setHeader("Content-Type","application/json");
        httpGet.setHeader("Authorization",token);

        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            finalString= EntityUtils.toString(entity, "UTF-8");
            try {
                httpResponse.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(finalString);
        return finalString;
    }
}
