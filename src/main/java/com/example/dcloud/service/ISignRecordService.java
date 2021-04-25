package com.example.dcloud.service;

import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.SignRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface ISignRecordService extends IService<SignRecord> {

    /**
     * 返回是否成功
     * @param id
     * @param startTime
     * @param cid
     * @param sids
     * @return
     */
    void initSignRecords(Integer id, LocalDateTime startTime, Integer cid, List<Integer> sids);

    /**
     * 修改学生状态
     * @param signId
     * @param studentIds
     * @param status
     */
    RespBean changeStatus(Integer signId, List<Integer> studentIds, Integer status);
}
