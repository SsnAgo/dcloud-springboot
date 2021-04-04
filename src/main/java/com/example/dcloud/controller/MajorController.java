package com.example.dcloud.controller;


import com.example.dcloud.pojo.Major;
import com.example.dcloud.service.IMajorService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
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
 * @since 2021-03-29
 */
@RestController
@RequestMapping("/major")
public class MajorController {

    @Resource
    private IMajorService majorService;

    @ApiOperation("获取所有专业列表")
    @GetMapping("/")
    public List<Major> getMajors(){
        return majorService.list();
    }

}
