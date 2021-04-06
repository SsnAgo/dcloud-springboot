package com.example.dcloud.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud.pojo.Dict;
import com.example.dcloud.pojo.DictInfo;
import com.example.dcloud.service.IDictInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
@RestController
@Api(tags = "DictInfoController")
@RequestMapping("/dict/manage/info")
public class DictInfoController {
    @Resource
    private IDictInfoService dictInfoService;


    @ApiOperation("根据tag获取字典项列表")
    @GetMapping("/{tag}")
    public List<DictInfo> getDictInfo(@PathVariable String tag){
        return dictInfoService.list(new QueryWrapper<DictInfo>().eq("tag", tag));
    }



}
