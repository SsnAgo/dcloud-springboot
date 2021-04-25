package com.example.dcloud.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.anotation.StudentAllow;
import com.example.dcloud.anotation.TeacherAllow;
import com.example.dcloud.pojo.*;
import com.example.dcloud.service.ICourseStudentService;
import com.example.dcloud.service.ISettingSignService;
import com.example.dcloud.service.ISignRecordService;
import com.example.dcloud.service.ISignService;
import com.example.dcloud.utils.SignUtils;
import com.example.dcloud.utils.UserUtils;
import com.example.dcloud.vo.SignHistoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cglib.core.Local;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.System;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@RestController
@Api(tags = "SignController")
@RequestMapping("/sign")
public class SignController {

    @Resource
    private ISignService signService;
    @Resource
    private ISignRecordService signRecordService;
    @Resource
    private ISettingSignService settingSignService;
    @Resource
    private ICourseStudentService courseStudentService;

    @Secured({"ROLE_TEACHER"})
    @ApiOperation("创建一个一键签到")
    @PostMapping("/create/nolimit")
    public RespBean startNoLimitSign(@RequestParam("id") @ApiParam("传入班课id") Integer cid,@RequestParam("local")@ApiParam("传入位置参数")String local) {
        if (!canCreate(cid)) {
            return RespBean.error("签到还在进行中，清勿重复发起");
        }
        Sign sign = new Sign();
        sign.setStartTime(LocalDateTime.now()).setEndTime(null).setLocal(local).setEnabled(true).setCourseId(cid).setCode(null).setType(SignUtils.NO_LIMIT);
        // 创建一条发起签到记录
        if (courseStudentService.count(new QueryWrapper<CourseStudent>().eq("cid",cid)) == 0){
            return RespBean.error("班级内暂无学生");
        }
        signService.save(sign);

        initRecords(sign.getId(),sign.getStartTime(),cid);
        return RespBean.success("创建签到成功",sign);
    }

    public void initRecords(Integer signId,LocalDateTime startTime,Integer courseId){
        // 获取该班课id所有学生 统一插入signrecord表 并将状态初始化为 0
        // 1.获取班课所有学生id列表
        List<Integer> sids = courseStudentService.listSidByCid(courseId);
        // 2.根据这些id进行批量插入
        signRecordService.initSignRecords(signId,startTime,courseId,sids);
    }

    @Secured({"ROLE_TEACHER"})
    @ApiOperation("传入限时分钟，创建一个限时签到")
    @PostMapping("/create/oneminute")
    public RespBean startOneMinuteSign(@RequestParam("id") @ApiParam("传入班课id") Integer cid,@RequestParam("timeout") @ApiParam("限时 单位分钟") Integer timeout,@RequestParam("local")@ApiParam("传入位置参数") String local) {
        if (!canCreate(cid)) {
            return RespBean.error("签到还在进行中，清勿重复发起");
        }
        Sign sign = new Sign();
        sign.setStartTime(LocalDateTime.now()).setEndTime(LocalDateTime.now().plusMinutes(timeout)).setLocal(local).setEnabled(true).setCourseId(cid).setCode(null).setType(SignUtils.TIME_LIMIT);
        if (courseStudentService.count(new QueryWrapper<CourseStudent>().eq("cid",cid)) == 0){
            return RespBean.error("班级内暂无学生");
        }
        signService.save(sign);

        initRecords(sign.getId(),sign.getStartTime(),cid);
        return RespBean.success("创建签到成功",sign);
    }

//    @Secured({"ROLE_TEACHER"})
//    @ApiOperation("创建一个手势签到")
//    @PostMapping("/create/hand")
//    public RespBean startHandSign(@RequestParam("id") @ApiParam("传入班课id") Integer cid, @RequestParam("sequence") @ApiParam("手势数字序列") String sequence) {
//        if (!canCreate(cid)) {
//            return RespBean.error("签到还在进行中，清勿重复发起");
//        }
//        Sign sign = new Sign();
//        sign.setStartTime(LocalDateTime.now()).setEndTime(null).setLocal(null).setEnabled(true).setCourseId(cid).setCode(sequence).setType(SignUtils.HAND);
//        signService.save(sign);
//        initRecords(sign.getId(),sign.getStartTime(),cid);
//        return RespBean.success("创建签到成功",sign);
//    }

//    @Secured({"ROLE_TEACHER"})
//    @ApiOperation("创建一个位置签到")
//    @PostMapping("/create/location")
//    public RespBean startHandSign(@RequestParam("id") @ApiParam("传入班课id") Integer cid) {
//        if (!canCreate(cid)) {
//            return RespBean.error("签到还在进行中，清勿重复发起");
//        }
//        Sign sign = new Sign();
//        sign.setStartTime(LocalDateTime.now()).setEndTime(null).setLocal(null).setEnabled(true).setCourseId(cid).setCode(null).setType(SignUtils.LOCATION);
//        signService.save(sign);
//        initRecords(sign.getId(),sign.getStartTime(),cid);
//        return RespBean.success("创建签到成功",sign);
//    }


    public Boolean canCreate(Integer cid) {
        // 查出该班课已经有的正在进行的签到
        Sign exist = signService.getOne(new QueryWrapper<Sign>().eq("courseId", cid).eq("enabled", true));
        // 如果存在可用的，说明要么正在可用  要么还没被检测出来过期  现在检测一下过期
        if (null != exist) {
            // 如果已经过期 将这个enabled置为null
            // 如果是由endtime的 就是有限时的 就检查有无过期
            if (exist.getType() == SignUtils.TIME_LIMIT && exist.getEndTime() != null) {
                if (LocalDateTime.now().isAfter(exist.getEndTime())) {
                    exist.setEnabled(false);
                    signService.updateById(exist);
                    return true;
                } else {
                    // 存在且没过期
                    return false;
                }
            }
            // 如果是无限时的 且存在 那就不能创建
            else {
                return false;
            }

        }
        // 不存在可用的就可以创建 返回 true
        else {
            return true;
        }
    }

    @ApiOperation("查看有无签到 有则进入签到页面，并返回该签到对象 0一键签到 1限时 2手势 3 位置")
    @GetMapping("/haveSign/{cid}")
    public RespBean canSign(@PathVariable Integer cid) {
        Sign exist = signService.getOne(new QueryWrapper<Sign>().eq("courseId", cid).eq("enabled", true));
        // 判断有无还可用的签到  如果不可用了 就返回不存在
        if (null == exist) {
            return RespBean.error("暂无发起签到或签到已结束");
        }
        // 如果可用 但是是有时间的 就判断一下有无过期
        if (exist.getEndTime() != null && exist.getType() == SignUtils.TIME_LIMIT) {
            // 如果过期了 就设置为false
            if (LocalDateTime.now().isAfter(exist.getEndTime())) {
                exist.setEnabled(false);
                signService.updateById(exist);
                return RespBean.error("暂无发起签到或签到已结束");
            }
        }
        return RespBean.success("可以签到", exist);
    }


    @ApiOperation("学生进行无限制签到")
    @GetMapping("/nolimit")
    public RespBean noLimitSign(@ApiParam("当前签到id") @RequestParam("id") Integer id,@RequestParam("local")@ApiParam("传入位置参数")String local) {
        System.out.println("位置参数为：" + local);
        User student = UserUtils.getCurrentUser();
        return signService.noLimitSign(id, student.getId(),local);
    }

    @ApiOperation("学生进行限时签到")
    @GetMapping("/time")
    public RespBean timeLimitSign(@ApiParam("当前签到id") @RequestParam("id") Integer id,@RequestParam("local") @ApiParam("传入位置参数")String local) {
        User student = UserUtils.getCurrentUser();
        return signService.timeLimitSign(id, student.getId(),local);
    }

//    @ApiOperation("学生进行手势签到")
//    @GetMapping("/hand")
//    public RespBean handSign(@ApiParam("当前班课id 参数名为cid") @RequestParam Integer cid,
//                             @ApiParam("手势序列，参数名为sequence")@RequestParam String sequence) {
//        User student = UserUtils.getCurrentUser();
//        return signService.handSign(cid,student.getId(),sequence);
//    }

    @Secured({"ROLE_TEACHER"})
    @ApiOperation("教师关闭签到")
    @GetMapping("/close")
    public RespBean closeSign(@ApiParam("当前签到id 参数名为id") @RequestParam("id") Integer id,
                              @ApiParam("0为放弃，1为关闭") @RequestParam("type") Integer type){
        return signService.closeSign(id,type);
    }


    @TeacherAllow
    @ApiOperation("教师根据签到id查看签到结果详情")
    @GetMapping("/records/{id}")
    public RespBean signInfo(@PathVariable @ApiParam("签到id") Integer id){
        return signService.signInfo(id);
    }

    @TeacherAllow
    @ApiOperation("实时签到人数")
    @GetMapping("/count/{id}")
    public RespBean showCount(@PathVariable @ApiParam("签到id") Integer id){
        return signService.showCount(id);
    }

    @TeacherAllow
    @ApiOperation("获取该班课所有签到记录")
    @GetMapping("/course/history/{cid}")
    public List<SignHistoryVo> getCourseHistory(@PathVariable @ApiParam("班课id") Integer cid){
        return signService.getCourseHistory(cid);
    }


    @ApiOperation("查看学生在某班课的签到记录")
    @GetMapping("/course")
    public List<SignRecord> getStudentHistory(@RequestParam("cid") @ApiParam("班课id") Integer cid,
                                                 @RequestParam("sid") @ApiParam("学生id") Integer sid){
        return signService.getStudentHistory(cid,sid);
    }


    @ApiOperation("根据签到id获取限时签到的剩余时间")
    @GetMapping("/time/{id}")
    public Integer timeAvailable(@PathVariable Integer id){
        return signService.timeAvailable(id);
    }



    @ApiOperation("修改学生签到状态")
    @PutMapping("/status")
    public RespBean changeStatus(@RequestParam("signId") @ApiParam("签到id") Integer signId,@RequestParam("studentIds")@ApiParam("多个学生id（传数组，也可单个）") List<Integer> studentIds,
                                 @RequestParam("status") @ApiParam("要修改的状态") Integer status){
        return signRecordService.changeStatus(signId,studentIds,status);
    }














}
