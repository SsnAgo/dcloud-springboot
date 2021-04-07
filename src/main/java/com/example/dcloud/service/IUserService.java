package com.example.dcloud.service;

import com.example.dcloud.pojo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface IUserService extends IService<User> {
    /**
     * 管理端通过用户名或手机号登录
     * @param usernameOrPhone
     * @param password
     * @param request
     * @return
     */
    RespBean manageLoginByPassword(String usernameOrPhone, String password,HttpServletRequest request);

    /**
     * 补全user信息
     * @param user
     * @return
     */
    User getUserInfo(User user);
    /**
     * 管理端通过验证码登录
     * @param phone
     * @param code
     * @param request
     * @return
     */
    RespBean manageLoginByCode(String phone,String code,HttpServletRequest request);

    /**
     * 通过电话号码或者username来获取该用户
     * @param usernameOrPhone
     * @return
     */
    User getUserByUsernameOrPhone(String usernameOrPhone);

    /**
     * 手机端通过账号密码登录
     * @param usernameOrPhone
     * @param password
     * @param request
     * @return
     */
    RespBean mobileLoginByPassword(String usernameOrPhone, String password, HttpServletRequest request);

    /**
     * 手机端通过验证码登录
     * @param phone
     * @param code
     * @param request
     * @return
     */
    RespBean mobileLoginByCode(String phone, String code, HttpServletRequest request);



    /**
     * 获取用户性别
     * @param user
     * @return
     */
    String getSex(User user);

    /**
     * 获取用户学历
     * @param user
     * @return
     */
    String getEducation(User user);

    /**
     * 获取学校
     * @param user
     * @return
     */
    School getSchool(User user);

    /**
     * 获取部门
     * @param user
     * @return
     */
    Department getDepartment(User user);


    /**
     * 获取专业
     * @param user
     * @return
     */
    Major getMajor(User user);


    /**
     * 分页获取所有用户信息
     * @param currentPage
     * @param size
     * @param user
     * @return
     */
    RespPageBean getUsersByPage(Integer currentPage, Integer size, User user);

    /**
     * 获取用户角色
     * @param user
     * @return
     */
    Role getRole(User user);

    /**
     * 更新用户头像
     * @param url
     * @param id
     * @param authentication
     * @return
     */
    RespBean updateUserFace(String url, Integer id, Authentication authentication);

    /**
     * 用户修改密码
     *
     * @param id
     * @param oldPassword
     * @param newPassword
     * @return
     */
    RespBean changePassword(Integer id, String oldPassword, String newPassword);


    /**
     * 登录时获取验证码，首先验证手机号存在
     * @param phone
     * @param request
     * @return
     */
    RespBean getLoginCaptcha(String phone, HttpServletRequest request);

    /**
     * 注册时获取验证码，首先验证手机号不存在
     * @param phone
     * @param request
     * @return
     */
    RespBean getRegisterCaptcha(String phone, HttpServletRequest request);
}
