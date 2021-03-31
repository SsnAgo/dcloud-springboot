package com.example.dcloud.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud.pojo.Dict;
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
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 按条件查询分页  没有则查全部
     * @param page
     * @param dict
     * @return
     */
    IPage<Dict> listDictPage(Page<Dict> page, @Param("dict") Dict dict);
}
