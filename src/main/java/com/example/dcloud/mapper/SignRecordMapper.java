package com.example.dcloud.mapper;

import com.example.dcloud.dto.SignStudentDto;
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
     * 获取该班课所有的学生的签到情况
     * @param courseId
     * @param signId
     * @return
     */
    List<SignStudentDto> listSign(@Param("courseId") Integer courseId,@Param("signId") Integer signId);

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
}
