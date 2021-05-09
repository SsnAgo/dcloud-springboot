package com.example.dcloud.mapper;

import com.example.dcloud.dto.SignStudentDto;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.SignRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface SignRecordMapper extends BaseMapper<SignRecord> {

    /**
     * 获取本次签到所有的学生的签到情况
     * @param signId
     * @return
     */
    List<SignStudentDto> listSign(@Param("signId") Integer signId);

    /**
     * 批量初始化签到情况
     * @param signId
     * @param startTime
     * @param cid
     * @param sids
     */
    void initSignRecords(@Param("signId") Integer signId,@Param("startTime") LocalDateTime startTime, @Param("cid") Integer cid, @Param("sids") List<Integer> sids);

    /**
     * 从签到记录里面删除 signid为id的
     * @param id
     */
    void deleteBySignId(Integer id);

    /**
     *  @param signId
     * @param studentIds
     * @param status
     * @return
     */
    boolean changeStatus(@Param("signId")Integer signId,@Param("studentIds") List<Integer> studentIds, @Param("status") Integer status);

    /**
     * 设置状态为已签到并设置当前时间
     * @param signId
     * @param studentIds
     * @param signed
     * @return
     */
    boolean changeToSigned(@Param("signId")Integer signId,@Param("studentIds") List<Integer> studentIds,@Param("status") Integer signed,@Param("time") LocalDateTime localDateTime);

    /**
     * 获取学生签到历史
     * @param cid
     * @param sid
     * @return
     */
    List<SignRecord> getStudentHistory(@Param("cid") Integer cid,@Param("sid") Integer sid);
}
