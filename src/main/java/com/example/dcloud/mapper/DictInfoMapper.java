package com.example.dcloud.mapper;

import com.example.dcloud.pojo.DictInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface DictInfoMapper extends BaseMapper<DictInfo> {

    /**
     * 删除tag对应的dictinfo
     * @param tag
     * @return
     */
    Integer deleteDictInfo(String tag);
}
