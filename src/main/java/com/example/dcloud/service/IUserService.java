package com.example.dcloud.service;

import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.Role;
import com.example.dcloud.pojo.User;
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
     * 获取该user的roles
     * @return
     */
    List<Role> getRoles(Integer userId);


}
