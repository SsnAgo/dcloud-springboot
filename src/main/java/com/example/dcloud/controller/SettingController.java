package com.example.dcloud.controller;


import com.example.dcloud.mapper.SettingMapper;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.SettingLevel;
import com.example.dcloud.pojo.SettingSign;
import com.example.dcloud.service.ISettingLevelService;
import com.example.dcloud.service.ISettingSignService;
import com.example.dcloud.vo.SettingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@Api(tags = "SettingController")
@RequestMapping("/setting/manage")
public class SettingController {

    @Resource
    private ISettingSignService settingSignService;
    @Resource
    private ISettingLevelService settingLevelService;
    @Resource
    private SettingMapper settingMapper;

    @ApiOperation("获取系统设置")
    @GetMapping("/")
    public SettingVo getSetting() {
        SettingSign settingSign = settingSignService.getById(1);
        List<SettingLevel> settingLevelList = settingLevelService.list();
        SettingVo settingVo = new SettingVo();
        settingVo.setSettingSign(settingSign);
        settingVo.setSettingLevelList(settingLevelList);
        return settingVo;
    }

    @ApiOperation("修改系统设置,数据要连贯,两边都是闭区间")
    @PutMapping("/")
    public RespBean updateSetting(@RequestBody SettingVo settingVo) {
        // 首先判断数据合不合理 范围要连贯且加起来是100
        List<SettingLevel> settingLevelList = settingVo.getSettingLevelList();
        int start = 0;
        settingLevelList.sort(Comparator.comparingInt(SettingLevel::getLeftBorder));
        for (SettingLevel settingLevel : settingLevelList) {
            if (settingLevel.getLeftBorder() != start) {
                return RespBean.error("范围数据错误(区间不连贯)");
            }
            start = settingLevel.getRightBorder() + 1;
        }
        if (start - 1 != 100) {
            return RespBean.error("范围数据错误(必须以100结束)");
        }
        if (settingMapper.updateSetting(settingVo)) {
            return RespBean.success("修改设置成功");
        }
        return RespBean.error("修改设置失败");
    }


}
