package com.example.dcloud.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.SettingSign;
import com.example.dcloud.pojo.Sign;
import com.example.dcloud.pojo.User;
import com.example.dcloud.service.ISettingSignService;
import com.example.dcloud.service.ISignRecordService;
import com.example.dcloud.service.ISignService;
import com.example.dcloud.utils.SignUtils;
import com.example.dcloud.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cglib.core.Local;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @Secured({"ROLE_TEACHER"})
    @ApiOperation("创建一个无限制签到")
    @PostMapping("/create/nolimit")
    public RespBean startNoLimitSign(@RequestParam("id") @ApiParam("传入班课id") Integer cid) {
        if (!canCreate(cid)) {
            return RespBean.error("签到还在进行中，清勿重复发起");
        }
        Sign sign = new Sign();
        sign.setStartTime(LocalDateTime.now()).setEndTime(null).setLocal(null).setEnabled(true).setCourseId(cid).setCode(null).setType(SignUtils.NO_LIMIT);
        signService.save(sign);
        return RespBean.success("创建签到成功",sign);
    }

    @Secured({"ROLE_TEACHER"})
    @ApiOperation("传入限时分钟，创建一个限时签到")
    @PostMapping("/create/oneminute")
    public RespBean startOneMinuteSign(@RequestParam("id") @ApiParam("传入班课id") Integer cid,@RequestParam("timeout") @ApiParam("限时 单位分钟") Integer timeout) {
        if (!canCreate(cid)) {
            return RespBean.error("签到还在进行中，清勿重复发起");
        }
        Sign sign = new Sign();
        sign.setStartTime(LocalDateTime.now()).setEndTime(LocalDateTime.now().plusMinutes(timeout)).setLocal(null).setEnabled(true).setCourseId(cid).setCode(null).setType(SignUtils.TIME_LIMIT);
        signService.save(sign);
        return RespBean.success("创建签到成功",sign);
    }

    @Secured({"ROLE_TEACHER"})
    @ApiOperation("创建一个手势签到")
    @PostMapping("/create/hand")
    public RespBean startHandSign(@RequestParam("id") @ApiParam("传入班课id") Integer cid, @RequestParam("sequence") @ApiParam("手势数字序列") String sequence) {
        if (!canCreate(cid)) {
            return RespBean.error("签到还在进行中，清勿重复发起");
        }
        Sign sign = new Sign();
        sign.setStartTime(LocalDateTime.now()).setEndTime(null).setLocal(null).setEnabled(true).setCourseId(cid).setCode(sequence).setType(SignUtils.HAND);
        signService.save(sign);
        return RespBean.success("创建签到成功",sign);
    }

    @Secured({"ROLE_TEACHER"})
    @ApiOperation("创建一个位置签到")
    @PostMapping("/create/location")
    public RespBean startHandSign(@RequestParam("id") @ApiParam("传入班课id") Integer cid) {
        if (!canCreate(cid)) {
            return RespBean.error("签到还在进行中，清勿重复发起");
        }
        Sign sign = new Sign();
        sign.setStartTime(LocalDateTime.now()).setEndTime(null).setLocal(null).setEnabled(true).setCourseId(cid).setCode(null).setType(SignUtils.LOCATION);
        signService.save(sign);
        return RespBean.success("创建签到成功",sign);
    }


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
    @ApiOperation("学生点击签到按钮查看有无签到 有则进入签到页面，并返回签到类型 0无限制 1限时 2手势 3 位置")
    @GetMapping("/cansign/{cid}")
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

        return RespBean.success("可以签到", exist.getType());
    }


    @ApiOperation("学生进行无限制签到")
    @GetMapping("/nolimit")
    public RespBean noLimitSign(@ApiParam("当前班课id") @RequestParam("cid") Integer cid) {
        User student = UserUtils.getCurrentUser();
        return signService.noLimitSign(cid, student.getId());
    }

    @ApiOperation("学生进行一分钟签到")
    @GetMapping("/oneminute")
    public RespBean oneMinuteSign(@ApiParam("当前班课id") @RequestParam("cid") Integer cid) {
        User student = UserUtils.getCurrentUser();
        return signService.oneMinuteSign(cid, student.getId());
    }

    @ApiOperation("学生进行手势签到")
    @GetMapping("/hand")
    public RespBean handSign(@ApiParam("当前班课id 参数名为cid") @RequestParam Integer cid,
                             @ApiParam("手势序列，参数名为sequence")@RequestParam String sequence) {
        User student = UserUtils.getCurrentUser();
        return signService.handSign(cid,student.getId(),sequence);
    }

    @Secured({"ROLE_TEACHER"})
    @ApiOperation("教师关闭签到")
    @GetMapping("/close")
    public RespBean closeSign(@ApiParam("当前班课id 参数名为cid") @RequestParam Integer cid){
        return signService.closeSign(cid);
    }




}
