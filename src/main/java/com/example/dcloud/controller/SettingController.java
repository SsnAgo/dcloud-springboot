package com.example.dcloud.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.mapper.SettingMapper;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.Setting;
import com.example.dcloud.pojo.SettingLevel;
import com.example.dcloud.pojo.SettingSign;
import com.example.dcloud.service.ISettingLevelService;
import com.example.dcloud.service.ISettingService;
import com.example.dcloud.service.ISettingSignService;
import com.example.dcloud.vo.SettingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@Api(tags = "SettingController")
@RequestMapping("/setting/manage")
public class SettingController {

    @Resource
    private ISettingService settingService;

    @ApiOperation("获取系统设置")
    @GetMapping("/")
    public List<Setting> getSettings(){
        return settingService.list();
    }

    @ApiOperation("新增系统设置")
    @PostMapping("/")
    public RespBean addSetting(@RequestBody Setting setting){
        if (!StringUtils.hasText(setting.getName())) {
            return RespBean.error("名称不能为空");
        }
        if (!StringUtils.hasText(setting.getKeyword())) {
            return RespBean.error("关键字不能为空");
        }
        if (!StringUtils.hasText(setting.getValue())) {
            return RespBean.error("值不能为空");
        }
        Setting exist = null;
        exist = settingService.getOne(new QueryWrapper<Setting>().eq("name",setting.getName()));
        if (exist != null) {
            return RespBean.error("已有相同名称的设置");
        }
        exist = settingService.getOne(new QueryWrapper<Setting>().eq("keyword",setting.getKeyword()));
        if (exist != null) {
            return RespBean.error("已有相同关键字的设置");
        }
        if (settingService.save(setting)) {
            return RespBean.success("新增设置成功");
        }
        return RespBean.error("新增失败");



    }

    @ApiOperation("修改系统设置")
    @PutMapping("/")
    public RespBean editSetting (@RequestBody Setting setting){
        if (!StringUtils.hasText(setting.getName())) {
            return RespBean.error("名称不能为空");
        }
        if (!StringUtils.hasText(setting.getValue())) {
            return RespBean.error("值不能为空");
        }
        if (settingService.updateById(setting)) {
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败");

    }
    @ApiOperation("删除系统设置")
    @DeleteMapping("/{id}")
    public RespBean delSetting (@PathVariable Integer id) {
        if (settingService.removeById(id)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation("检查名称是否可用")
    @GetMapping("/ckname/{name}")
    public RespBean checkName(@PathVariable String name){
        if (settingService.getOne(new QueryWrapper<Setting>().eq("name",name)) != null) {
            return RespBean.error("名称重复",false);
        }
        return null;
    }


    @ApiOperation("检查关键字是否可用")
    @GetMapping("/ckkwd/{keyword}")
    public RespBean checkKwd(@PathVariable String keyword){
        if (!keyword.matches("^[a-zA-Z][a-zA-Z0-9_]*$")){
            return RespBean.error("关键词必须为英文或数字,且以英文开头",false);
        }
        if (settingService.getOne(new QueryWrapper<Setting>().eq("keyword",keyword)) != null) {
            return RespBean.error("关键词重复",false);
        }
        return null;
    }
}
