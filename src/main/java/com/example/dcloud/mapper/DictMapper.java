package com.example.dcloud.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud.pojo.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dcloud.pojo.DictInfo;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.vo.DictVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * @param search
     * @return
     */
    IPage<Dict> listDictPage(Page<Dict> page, @Param("search") String search);

    /**
     * 添加字典及其字典项
     * @param dict
     * @param dictInfoList
     * @return
     */
    boolean insertDictAndDictInfo(@Param("dict") Dict dict,@Param("dictInfoList") List<DictInfo> dictInfoList);

    /**
     * 修改字典及其字典项
     * @param dict
     * @param dictInfoList
     * @return
     */
    boolean updateDictAndDictInfo(@Param("dict") Dict dict,@Param("dictInfoList") List<DictInfo> dictInfoList);



}
