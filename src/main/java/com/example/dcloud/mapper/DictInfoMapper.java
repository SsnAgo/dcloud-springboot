package com.example.dcloud.mapper;

import com.example.dcloud.pojo.DictInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface DictInfoMapper extends BaseMapper<DictInfo> {

    /**
     *
     * @param tag
     * @param dictInfoList
     * @return
     */
    boolean insertDictInfo(@Param("tag") String tag,@Param("dictInfoList") List<DictInfo> dictInfoList);

    /**
     * 更新字典项
     * @param tag
     * @param dictInfoList
     * @return
     */
    boolean updateDictInfo(@Param("tag")String tag,@Param("dictInfoList") List<DictInfo> dictInfoList);
}
