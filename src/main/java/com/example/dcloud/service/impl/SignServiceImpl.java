package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.dto.SignStudentDto;
import com.example.dcloud.mapper.*;
import com.example.dcloud.pojo.*;
import com.example.dcloud.service.ISignService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloud.utils.DistanceUtil;
import com.example.dcloud.utils.SignUtils;
import com.example.dcloud.utils.WeekDayUtils;
import com.example.dcloud.vo.SignHistoryVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@Service
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign> implements ISignService {

    @Resource
    private SignMapper signMapper;
    @Resource
    private SignRecordMapper signRecordMapper;
    @Resource
    private SettingSignMapper settingSignMapper;
    @Resource
    private CourseStudentMapper courseStudentMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public RespBean noLimitSign(Integer signId, Integer sid,String local) {
        // 由于这个时候是点击签到  所以还得判断一下这个签到还能不能用 由于是无限制签到 所以只需要判断是否enabled
        Sign exist = signMapper.selectOne(new QueryWrapper<Sign>().eq("id", signId).eq("enabled", true));
        if (exist == null){
            return RespBean.error("签到已结束，请联系教师");
        }
        Double distance = DistanceUtil.getDistanceMeter(exist.getLocal(),local);
        Double settingDistance = settingSignMapper.selectById(1).getSignDistance();
        if (settingDistance == 0 || distance <= settingDistance){
            signSuccess(signId,sid,distance);
            return RespBean.success("签到成功");
        }
        // 执行签到成功的数据库相关更新操作
        return RespBean.error("距离太远，签到失败");

    }

    @Override
    @Transactional
    public RespBean timeLimitSign(Integer signId, Integer sid,String local) {
        Sign exist = signMapper.selectOne(new QueryWrapper<Sign>().eq("id", signId).eq("enabled", true));
        if (exist == null) {
            return RespBean.error("签到已结束，请联系教师");
        }
        // 如果还可用，那么进行日期检查
        if (exist.getEndTime() != null && exist.getType()== SignUtils.TIME_LIMIT){
            if (LocalDateTime.now().isAfter(exist.getEndTime())){
                exist.setEnabled(false);
                signMapper.updateById(exist);
                return RespBean.error("签到已结束，请联系教师");
            }else{
                Double distance = DistanceUtil.getDistanceMeter(exist.getLocal(),local);
                Double settingDistance = settingSignMapper.selectById(1).getSignDistance();
                if (settingDistance == 0 || distance <= settingDistance){
                    signSuccess(signId,sid,distance);
                    return RespBean.success("签到成功");
                }
                // 执行签到成功的数据库相关更新操作
                return RespBean.error("距离太远，签到失败");
            }
        }
        return RespBean.error("发生错误，该签到类型不是限时签到");
    }

//    @Override
//    @Transactional
//    public RespBean handSign(Integer cid, Integer sid, String sequence,,String local) {
//        Sign exist = signMapper.selectOne(new QueryWrapper<Sign>().eq("courseId", cid).eq("enabled", true));
//        if (exist == null) {
//            return RespBean.error("签到已结束，请联系教师");
//        }
//        // 检验手势是否正确
//        if (exist.getType() == SignUtils.HAND && exist.getCode() != null ){
//            if (!exist.getCode().equals(sequence)){
//                return RespBean.error("手势错误，请重试");
//            }else{
//                signSuccess(cid,sid, exist.getId(),local);
//                return RespBean.success("签到成功");
//            }
//        }
//        return RespBean.error("发生错误，该签到类型不是手势签到");
//    }

    @Override
    @Transactional
    public RespBean closeSign(Integer cid, Integer type) {
        Sign exist = signMapper.selectOne(new QueryWrapper<Sign>().eq("courseId", cid).eq("enabled", true));
        if (exist == null) {
            return RespBean.error("没有可关闭的签到");
        }
        // 如果type是 1 关闭签到
        if (type == 1){
            exist.setEnabled(false);
            exist.setEndTime(LocalDateTime.now());
            signMapper.updateById(exist);
            return RespBean.success("关闭签到成功",exist);
        }
        if (type == 0) {
            signMapper.deleteById(exist.getId());
            signRecordMapper.deleteBySignId(exist.getId());
            return RespBean.success("放弃签到成功",exist);
        }
        return RespBean.error("无效关闭类型");

    }

    @Override
    @Transactional
    public RespBean signInfo(Integer signId) {
        // 获取该签到
        Sign sign = signMapper.selectById(signId);
        Integer courseId = sign.getCourseId();
        // 获取该班级所有成员的signStudentDto
        List<SignStudentDto> allList = signRecordMapper.listSign(courseId,signId);
        List<SignStudentDto> signedList = new ArrayList<>();
        List<SignStudentDto> unSignedList = new ArrayList<>();
        for (SignStudentDto item : allList) {
            if (item.getStatus() == SignUtils.SIGNED){
                signedList.add(item);
            }else{
                unSignedList.add(item);
            }
        }
        Map<String,Object> res = new HashMap<>();
        res.put("total",allList.size());
        res.put("signedCount",signedList.size());
        res.put("signedList",signedList);
        res.put("unSignedList",unSignedList);
        return RespBean.success("查看成功",res);
    }

    @Override
    public RespBean showCount(Integer signId) {
        // 获取该签到
        Sign sign = signMapper.selectById(signId);
        // 获取该班级总人数
        Integer total = courseStudentMapper.selectCount(new QueryWrapper<CourseStudent>().eq("cid",sign.getCourseId()));
        Integer signedCount = signRecordMapper.selectCount(new QueryWrapper<SignRecord>().eq("status",SignUtils.SIGNED));
        Map<String,Integer> res = new HashMap<>();
        res.put("total",total);
        res.put("signedCount",signedCount);
        return RespBean.success("获取成功",res);
    }

    @Override
    public List<SignHistoryVo> getCourseHistory(Integer cid) {
        List<SignHistoryVo> signHistoryVoList = signMapper.getCourseHistory(cid);
        Integer total = courseStudentMapper.selectCount(new QueryWrapper<CourseStudent>().eq("courseId", cid));
        for (SignHistoryVo hist : signHistoryVoList) {
            Integer signedCount = signRecordMapper.selectCount(new QueryWrapper<SignRecord>().eq("signId", hist.getSignId()).eq("status",SignUtils.SIGNED));
            hist.setSignedCount(signedCount);
            hist.setTotal(total);
            hist.setDayOfWeek(WeekDayUtils.num2word(hist.getStartTime().getDayOfWeek().getValue()));
        }
        return signHistoryVoList;
    }
    @Override
    public List<SignRecord> getStudentHistory(Integer cid, Integer sid) {
        List<SignRecord> signRecordList = signRecordMapper.selectList(new QueryWrapper<SignRecord>().eq("courseId",cid).eq("studentId",sid));
        for (SignRecord record : signRecordList) {
            // 如果有签到  就设置为签到的时间
            if (record.getSignTime() != null){
                record.setDayOfWeek(WeekDayUtils.num2word(record.getSignTime().getDayOfWeek().getValue()));
            }
            // 未签到 就设置为签到发起的时间
            else{
                record.setDayOfWeek(WeekDayUtils.num2word(record.getStartTime().getDayOfWeek().getValue()));
            }
        }
        return signRecordList;
    }




    @Transactional
    public void signSuccess(Integer signId,Integer sid,Double distance){
        // 执行签到成功的各个更新操作  更新到signrecoreds里面 同时获取系统经验值，根据系统经验值增加经验， 更新学生在该班课的经验值 更新学生的总经验值
        SignRecord record = signRecordMapper.selectOne(new QueryWrapper<SignRecord>().eq("signId",signId).eq("studentId",sid));
        // 获取系统设置的签到经验值
        Integer exp = settingSignMapper.selectById(1).getSignExp();
        // 更新状态为签到
        record.setSignTime(LocalDateTime.now()).setAddExp(exp).setDistance(distance).setStatus(SignUtils.SIGNED);
        signRecordMapper.updateById(record);
        //更新该学生在该班级的经验
        CourseStudent courseStudent = courseStudentMapper.selectOne(new QueryWrapper<CourseStudent>().eq("signId", signId).eq("studentId", sid));
        courseStudent.setExp(courseStudent.getExp() + exp);
        courseStudentMapper.updateById(courseStudent);
        // 更新学生的总经验值
        User student = userMapper.selectById(sid);
        student.setExp(student.getExp() + exp);
        userMapper.updateById(student);
    }
}
