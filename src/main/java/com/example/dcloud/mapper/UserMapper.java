package com.example.dcloud.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface UserMapper extends BaseMapper<User> {


    /**
     * 分页获取数据
     *
     * @param currentUserId
     * @param page
     * @param search
     * @return
     */
    IPage<User> getUsersByPage(@Param("currentUserId") Integer currentUserId, Page<User> page, @Param("search") String search);
}
