package com.example.dcloud.service;

import com.example.dcloud.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface IRoleService extends IService<Role> {

    /**
     * 如果role为null则查询全部，否则按照role.nameZh 和 enabled查询
     * @param search
     * @return
     */
    List<Role> getRoles(String search);
}
