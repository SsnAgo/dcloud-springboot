package com.example.dcloud.mapper;

import com.example.dcloud.pojo.Sign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dcloud.vo.SignHistoryVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface SignMapper extends BaseMapper<Sign> {

    /**
     * 获取班课历史签到记录
     * @param cid
     * @return
     */
    List<SignHistoryVo> getCourseHistory(Integer cid);

}
