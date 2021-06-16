package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.mapper.*;
import com.example.dcloud.pojo.*;
import com.example.dcloud.service.ISignRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloud.utils.SignUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@Service
public class SignRecordServiceImpl extends ServiceImpl<SignRecordMapper, SignRecord> implements ISignRecordService {

    @Resource
    private SignRecordMapper signRecordMapper;
    @Resource
    private SettingSignMapper settingSignMapper;
    @Resource
    private SignMapper signMapper;
    @Resource
    private CourseStudentMapper courseStudentMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private SettingMapper settingMapper;

    @Override
    public void initSignRecords(Integer signId, LocalDateTime startTime, Integer cid, List<Integer> sids) {
        signRecordMapper.initSignRecords(signId, startTime, cid, sids);
    }

    @Override
    @Transactional
    public RespBean changeStatus(Integer signId, List<Integer> studentIds, Integer status) {
        List<SignRecord> signRecords = signRecordMapper.selectList(new QueryWrapper<SignRecord>().eq("signId", signId));
        Sign sign = signMapper.selectById(signId);
        Integer courseId = sign.getCourseId();
        LocalDateTime startTime = sign.getStartTime();
        // 获取所有这个签到下  已有签到记录的sids （包括签到和签到失败的 以及其他被设置状态了的）
        List<Integer> sidList = signRecords.stream().map(SignRecord::getStudentId).collect(Collectors.toList());
        // 放到set里  方便查询contains
        Set<Integer> sidSet = new HashSet<>(sidList);
        // 备用
        SignRecord temp;
        Map<Integer, Integer> map = initStatusExpMap();
        try {
            for (Integer studentId : studentIds) {
                // 如果修改已经有记录的 就表示已经有签到时间了 直接修改状态即可  否则用insert新增这条记录
                if (sidSet.contains(studentId)) {
                    temp = signRecordMapper.selectOne(new QueryWrapper<SignRecord>().eq("signId", signId).eq("studentId", studentId));
                    // 已有状态和现有状态一样 跳过本项
                    if (temp.getStatus() == status) {
                        continue;
                    }
                    Integer changeExp = map.get(status) - temp.getAddExp();
                    // 状态不一样  则判断处于什么状态  更改状态的同时 也要修改各个经验值
                    temp.setStatus(status).setAddExp(map.get(status)).setSignTime(LocalDateTime.now());
                    signRecordMapper.updateById(temp);
                    changeStudentExp(studentId,courseId,changeExp);
                } else {
                    temp = new SignRecord();
                    temp.setSignId(signId).setCourseId(courseId).setStudentId(studentId).setStartTime(startTime).setSignTime(LocalDateTime.now()).setStatus(status).setAddExp(map.get(status)).setDistance(null);
                    signRecordMapper.insert(temp);
                    changeStudentExp(studentId,courseId,map.get(status));
                }
            }
            return RespBean.success("修改状态成功");
        }catch (Exception e){
            e.printStackTrace();
            return RespBean.error("修改状态失败");
        }
    }

    /**
     * 初始化 签到状态：签到经验值  的map
     * @return
     */
    private Map<Integer, Integer> initStatusExpMap() {
        // 获取各个签到类型对应的经验值
        Setting settingSign = settingMapper.selectOne(new QueryWrapper<Setting>().eq("keyword","experience"));
        Integer signedExp = 2;
        if (settingSign != null) {
            signedExp = Integer.valueOf(settingSign.getValue());
        }
//        Integer dayOffExp = settingSign.getDayOffExp();
//        Integer lateExp = settingSign.getLateExp();
//        Integer leaveEarlyExp = settingSign.getLeaveEarlyExp();
        // 创建类型和经验值的map
        Map<Integer, Integer> signExpMap = new HashMap();
        signExpMap.put(SignUtils.SIGNED, signedExp);
        signExpMap.put(SignUtils.NO_SIGNED, 0);
        signExpMap.put(SignUtils.DAY_OFF, 0);
        signExpMap.put(SignUtils.LATE_IN, 0);
        signExpMap.put(SignUtils.EARLY_LEAVE, 0);
        return signExpMap;
//        temp.setStatus(status).setAddExp(signExpMap.get(status));
//        if (status == SignUtils.SIGNED){
//            temp.setStatus(SignUtils.SIGNED).setAddExp(signedExp);
//        }else if (status == SignUtils.NO_SIGNED){
//            temp.setStatus(SignUtils.NO_SIGNED).setAddExp(0);
//        }else if (status == SignUtils.DAY_OFF){
//            temp.setStatus(SignUtils.DAY_OFF).setAddExp(dayOffExp);
//        }
//        else if (status == SignUtils.LATE_IN){
//            temp.setStatus(SignUtils.LATE_IN).setAddExp(lateExp);
//        }
//        else if (status == SignUtils.EARLY_LEAVE){
//            temp.setStatus(SignUtils.EARLY_LEAVE).setAddExp(leaveEarlyExp);
//        }
//        return temp;
    }

    /**
     * 修改状态后，也要修改对应的学生的在改班课的经验值和总经验值 新增经验值则传正数 否则传负数
     * 需要学生id和班课id
     */
    private void changeStudentExp(Integer studentId,Integer courseId,Integer addExp){
        CourseStudent courseStudent = courseStudentMapper.selectOne(new QueryWrapper<CourseStudent>().eq("sid", studentId).eq("cid", courseId));
        User user = userMapper.selectById(studentId);
        // 更新班课经验值
        courseStudent.setExp(courseStudent.getExp() + addExp);
        courseStudentMapper.updateById(courseStudent);
        // 更新用户经验值
        user.setExp(user.getExp() + addExp);
        userMapper.updateById(user);
    }




}
