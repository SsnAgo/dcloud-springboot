package com.example.dcloud.service;

import com.example.dcloud.pojo.*;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
     * 通过用户名登录
     * @param username
     * @param password
     * @param request
     * @return
     */
    RespBean loginByUsername(String username, String password,HttpServletRequest request);


    /**
     * 补全user信息
     * @param user
     * @return
     */
    User getUserInfo(User user);
    /**
     * 登录接口
     * @param phone
     * @param code
     * @param request
     * @return
     */
    RespBean loginByPhone(String phone,String code,HttpServletRequest request);

    /**
     * 通过电话号码或者username来获取该用户
     * @param usernameOrPhone
     * @return
     */
    User getUserByUsernameOrPhone(String usernameOrPhone);

    /**
     * 补全一些和其他表关联的信息
     * @param user
     * @return
     */
    User getCompletedUser(User user);




    /**
     * 获取该user的roles
     * @return
     */
    List<Role> getRoles(Integer userId);

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
}
