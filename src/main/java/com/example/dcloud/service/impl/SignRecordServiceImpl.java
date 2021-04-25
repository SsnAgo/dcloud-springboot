package com.example.dcloud.service.impl;

import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.SignRecord;
import com.example.dcloud.mapper.SignRecordMapper;
import com.example.dcloud.service.ISignRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@Service
public class SignRecordServiceImpl extends ServiceImpl<SignRecordMapper, SignRecord> implements ISignRecordService {

    @Resource
    private SignRecordMapper signRecordMapper;
    @Override
    public void initSignRecords(Integer signId, LocalDateTime startTime, Integer cid, List<Integer> sids) {
        signRecordMapper.initSignRecords(signId,startTime,cid,sids);
    }

    @Override
    public RespBean changeStatus(Integer signId, List<Integer> studentIds, Integer status) {
        if (signRecordMapper.changeStatus(signId,studentIds,status)){
            return RespBean.success("修改状态成功");
        }
        return RespBean.error("修改状态失败");

    }
}
