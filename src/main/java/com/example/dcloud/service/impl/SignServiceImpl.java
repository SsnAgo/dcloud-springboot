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
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.Duration;
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
        if (signRecordMapper.selectOne(new QueryWrapper<SignRecord>().eq("signId",exist.getId()).eq("studentId",sid).eq("status",SignUtils.SIGNED)) != null) {
            return RespBean.success("已签到成功");
        }
        // 如果该签到没有位置信息  那就直接签到成失败 置位置信息为null
        if (!StringUtils.hasText(exist.getLocal()) || exist.getLocal().equals("undefined,undefined")){
            signSuccess(signId,sid,0.0);
            return RespBean.success("签到成功");
        }
        // 如果学生的位置没有签到信息，显示签到失败
        if (!StringUtils.hasText(local) || local.equals("undefined,undefined")) {
            signFailed(signId,sid,null);
            return RespBean.success("定位失败，签到失败");
        }
        Double distance = DistanceUtil.getDistanceMeter(exist.getLocal(),local);
        Double settingDistance = settingSignMapper.selectById(1).getSignDistance();
        if (settingDistance == 0 || distance <= settingDistance){
            // 执行签到成功的数据库相关更新操作
            signSuccess(signId,sid,Double.parseDouble(String.format("%.1f",distance)));
            return RespBean.success("签到成功");
        }else{
            signFailed(signId,sid,distance);
            return RespBean.error("距离太远，签到失败");
        }

    }

    @Override
    @Transactional
    public RespBean timeLimitSign(Integer signId, Integer sid,String local) {
        Sign exist = signMapper.selectOne(new QueryWrapper<Sign>().eq("id", signId).eq("enabled", true));
        if (exist == null) {
            return RespBean.error("签到已结束，请联系教师");
        }
        if (signRecordMapper.selectOne(new QueryWrapper<SignRecord>().eq("signId",exist.getId()).eq("studentId",sid).eq("status",SignUtils.SIGNED)) != null) {
            return RespBean.success("已签到成功");
        }
        // 如果还可用，那么进行日期检查
        if (exist.getEndTime() != null && exist.getType()== SignUtils.TIME_LIMIT){
            if (LocalDateTime.now().isAfter(exist.getEndTime())){
                exist.setEnabled(false);
                signMapper.updateById(exist);
                return RespBean.error("签到已结束，请联系教师");
            }else{
                // 如果该签到没有位置信息  那就直接签到成功 置距离为0
                if (exist.getLocal() == null  || exist.getLocal().equals("undefined,undefined")){
                    signSuccess(signId,sid,0.0);
                    return RespBean.success("签到成功");
                }
                // 如果学生的位置没有签到信息，显示签到失败
                if (local == null || local.equals("undefined,undefined")) {
                    signFailed(signId,sid,null);
                    return RespBean.success("定位失败，签到失败");
                }
                Double distance = DistanceUtil.getDistanceMeter(exist.getLocal(),local);
                Double settingDistance = settingSignMapper.selectById(1).getSignDistance();
                if (settingDistance == 0 || distance <= settingDistance){
                    signSuccess(signId,sid,Double.parseDouble(String.format("%.1f",distance)));
                    return RespBean.success("签到成功");
                }else{
                    // 距离太远 签到失败
                    signFailed(signId,sid,distance);
                    return RespBean.error("距离太远，签到失败");
                }

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
    public RespBean closeSign(Integer id, Integer type) {
        Sign exist = signMapper.selectOne(new QueryWrapper<Sign>().eq("id", id).eq("enabled", true));
        if (exist == null) {
            return RespBean.error("没有可关闭的签到");
        }
        // 如果type是 1 关闭签到
        if (type == SignUtils.CLOSE){
            exist.setEnabled(false);
            exist.setEndTime(LocalDateTime.now());
            signMapper.updateById(exist);
            return RespBean.success("关闭签到成功",exist);
        }
        if (type == SignUtils.GIVE_UP) {
            signMapper.deleteById(id);
            signRecordMapper.deleteBySignId(id);
            return RespBean.success("放弃签到成功",exist);
        }
        return RespBean.error("无效关闭类型");

    }

    @Override
    @Transactional
    public RespBean signInfo(Integer signId) {
        // 获取该签到
        Sign sign = signMapper.selectById(signId);
        // 通过查询该 cid的所有学生 通过学生左表连接 签到记录表，实现查询该班课里所有记录
        List<SignStudentDto> allList = signRecordMapper.listSign(signId);
        List<SignStudentDto> signedList = new ArrayList<>();
        List<SignStudentDto> unSignedList = new ArrayList<>();
        for (SignStudentDto item : allList) {
            if (item.getStatus() != null && item.getStatus() == SignUtils.SIGNED){
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
        Integer signedCount = signRecordMapper.selectCount(new QueryWrapper<SignRecord>().eq("status",SignUtils.SIGNED).eq("signId",signId));
        Map<String,Integer> res = new HashMap<>();
        res.put("total",total);
        res.put("signedCount",signedCount);
        return RespBean.success("获取成功",res);
    }

    @Override
    @Transactional
    public List<SignHistoryVo> getCourseHistory(Integer cid) {
        List<SignHistoryVo> signHistoryVoList = signMapper.getCourseHistory(cid);
        Integer total = courseStudentMapper.selectCount(new QueryWrapper<CourseStudent>().eq("cid", cid));
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

    @Override
    public Integer timeAvailable(Integer id) {
        Sign sign = signMapper.selectById(id);
        if (sign.getType() == SignUtils.TIME_LIMIT){
            // 如果还没结束 就返回剩余秒数
            if (LocalDateTime.now().isBefore(sign.getEndTime())){
                return Math.toIntExact(Duration.between( LocalDateTime.now(),sign.getEndTime()).getSeconds());
            }else {
                // 这个时候签到已经结束辽  调用关闭方法
                closeSign(id,SignUtils.CLOSE);
            }
        }else {
            return null;
        }
        return null;
    }


    @Transactional
    public void signSuccess(Integer signId,Integer sid,Double distance){
//        // 执行签到成功的各个更新操作  更新到signrecoreds里面 同时获取系统经验值，根据系统经验值增加经验， 更新学生在该班课的经验值 更新学生的总经验值
//        SignRecord record = signRecordMapper.selectOne(new QueryWrapper<SignRecord>().eq("signId",signId).eq("studentId",sid));
//        // 获取系统设置的签到经验值
//        Integer exp = settingSignMapper.selectById(1).getSignExp();
//        // 更新状态为签到
//        record.setSignTime(LocalDateTime.now()).setAddExp(exp).setDistance(distance).setStatus(SignUtils.SIGNED);
//        signRecordMapper.updateById(record);
        SignRecord record = new SignRecord();
        // 获取系统设置的签到经验值
        Integer exp = settingSignMapper.selectById(1).getSignExp();
        Sign sign = signMapper.selectById(signId);
        // 新增一条签到成功记录
        record.setSignTime(LocalDateTime.now()).setAddExp(exp).setDistance(distance).setStatus(SignUtils.SIGNED).setStartTime(sign.getStartTime()).setCourseId(sign.getCourseId()).setSignId(signId).setStudentId(sid);
        signRecordMapper.insert(record);
        //更新该学生在该班级的经验
        CourseStudent courseStudent = courseStudentMapper.selectOne(new QueryWrapper<CourseStudent>().eq("cid", record.getCourseId()).eq("sid", sid));
        courseStudent.setExp(courseStudent.getExp() + exp);
        courseStudentMapper.updateById(courseStudent);
        // 更新学生的总经验值
        User student = userMapper.selectById(sid);
        student.setExp(student.getExp() + exp);
        userMapper.updateById(student);
    }

    @Transactional
    // 距离太远或没有定位  签到失败  但是会有距离
    public void signFailed(Integer signId,Integer sid,Double distance){
        SignRecord record = new SignRecord();
        // 获取系统设置的签到经验值
        Sign sign = signMapper.selectById(signId);
        // 新增一条签到失败记录
        record.setSignTime(LocalDateTime.now()).setAddExp(0).setDistance(distance).setStatus(SignUtils.NO_SIGNED).setStartTime(sign.getStartTime()).setCourseId(sign.getCourseId()).setSignId(signId).setStudentId(sid);
        signRecordMapper.insert(record);
    }





}
