package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.mapper.*;
import com.example.dcloud.pojo.*;
import com.example.dcloud.service.ISignService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloud.utils.SignUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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
    public RespBean noLimitSign(Integer cid, Integer sid) {
        // 由于这个时候是点击签到  所以还得判断一下这个签到还能不能用 由于是无限制签到 所以只需要判断是否enabled
        Sign exist = signMapper.selectOne(new QueryWrapper<Sign>().eq("courseId", cid).eq("enabled", true));
        if (exist == null){
            return RespBean.error("签到已结束，请联系教师");
        }
        // 执行签到成功的数据库相关更新操作
        signSuccess(cid,sid);
        return RespBean.success("签到成功");
    }

    @Override
    public RespBean oneMinuteSign(Integer cid, Integer sid) {
        Sign exist = signMapper.selectOne(new QueryWrapper<Sign>().eq("courseId", cid).eq("enabled", true));
        if (exist == null) {
            return RespBean.error("签到已结束，请联系教师");
        }
        // 如果还可用，那么进行日期检查
        if (exist.getEndTime() != null && exist.getType()== SignUtils.ONE_MINUTE){
            if (LocalDateTime.now().isAfter(exist.getEndTime())){
                return RespBean.error("签到已结束，请联系教师");
            }else{
                signSuccess(cid,sid);
                return RespBean.success("签到成功");
            }
        }
        return RespBean.error("未知错误");
    }

    @Override
    public RespBean handSign(Integer cid, Integer sid, String sequence) {
        Sign exist = signMapper.selectOne(new QueryWrapper<Sign>().eq("courseId", cid).eq("enabled", true));
        if (exist == null) {
            return RespBean.error("签到已结束，请联系教师");
        }
        // 检验手势是否正确
        if (exist.getType() == SignUtils.HAND && exist.getCode() != null ){
            if (!exist.getCode().equals(sequence)){
                return RespBean.error("手势错误，请重试");
            }else{
                signSuccess(cid,sid);
                return RespBean.success("签到成功");
            }
        }
        return RespBean.error("未知错误");
    }

    @Override
    public RespBean closeSign(Integer cid) {
        Sign exist = signMapper.selectOne(new QueryWrapper<Sign>().eq("courseId", cid).eq("enabled", true));
        if (exist == null) {
            return RespBean.error("没有可关闭的签到");
        }
        // 关闭签到
        exist.setEnabled(false);
        signMapper.updateById(exist);
        return RespBean.success("关闭签到成功",exist);
    }


    public void signSuccess(Integer cid,Integer sid){
        // 执行签到成功的各个更新操作  存入到signrecoreds里面 同时获取系统经验值，根据系统经验值增加经验， 更新学生在该班课的经验值 更新学生的总经验值
        SignRecord record = new SignRecord();
        record.setCourseId(cid).setSignTime(LocalDateTime.now()).setStudentId(sid);
        // 新增一条签到记录
        signRecordMapper.insert(record);
        // 获取系统经验值，更新该学生在该班级的经验
        Integer exp = settingSignMapper.selectById(1).getSignExp();
        CourseStudent courseStudent = courseStudentMapper.selectOne(new QueryWrapper<CourseStudent>().eq("sid", sid).eq("cid", cid));
        courseStudent.setExp(courseStudent.getExp() + exp);
        courseStudentMapper.updateById(courseStudent);
        // 更新学生的总经验值
        User student = userMapper.selectById(sid);
        student.setExp(student.getExp() + exp);
        userMapper.updateById(student);
    }







}
