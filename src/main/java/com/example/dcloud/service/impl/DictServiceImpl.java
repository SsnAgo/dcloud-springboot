package com.example.dcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud.mapper.DictInfoMapper;
import com.example.dcloud.pojo.Dict;
import com.example.dcloud.mapper.DictMapper;
import com.example.dcloud.pojo.DictInfo;
import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.RespPageBean;
import com.example.dcloud.service.IDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloud.vo.DictVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

    @Resource
    private DictMapper dictMapper;
    @Resource
    private DictInfoMapper dictInfoMapper;

    @Override
    public RespPageBean listDict(Integer currentPage, Integer size, Dict dict) {
        Page<Dict> page = new Page<>(currentPage,size);
        IPage<Dict> dictPage = dictMapper.listDictPage(page,dict);
        RespPageBean respPageBean = new RespPageBean(dictPage.getTotal(),dictPage.getRecords());
        return respPageBean;
    }

    @Override
    @Transactional
    public RespBean updateDict(DictVo dictVo) {
        // 如果字典信息插入不成功，就直接return
        Dict dict = dictVo.getDict();
        List<DictInfo> dictInfoList = dictVo.getDictInfoList();
        // 除了目前的tagZh之外，不能改成其他已存在的
        // 判断当前是否有该tag 如果没有则不能修改
        Dict exits = dictMapper.selectOne(new QueryWrapper<Dict>().eq("tag", dict.getTag()));
        if (null == exits){
            return RespBean.error("要修改的字典不存在");
        }
        // 要修改的该tag存在，就判断中文标识是否存在 先查当前要改的字典在数据库中对应的中文标识
        String currentTagZh = dictMapper.selectOne(new QueryWrapper<Dict>().eq("tag",dict.getTag())).getTagZh();
        // 如果当前的不和目前的相同 那么要去判断有没有存在  有则不能改
        if (!currentTagZh.equals(dict.getTagZh())){
            // 查询数据库中有没有除了当前tag之外的 还有tagZh相同的项
            exits = dictMapper.selectOne(new QueryWrapper<Dict>().eq("tagZh",dict.getTagZh()).ne("tag",dict.getTag()));
            if (exits != null){
                return RespBean.error("更新字典失败，中文标识不能重复");
            }
        }
        System.out.println("开始更新字典项了");
        // 接下来更新字典项
        // 判断排序是否正确  默认值是否唯一 以及内容是否不重复
        Set<Integer> sequenceSet = new HashSet<>();
        Set<String> contentSet = new HashSet<>();
        Integer defaultCount = 0;
        for (DictInfo dictInfo : dictInfoList) {
            sequenceSet.add(dictInfo.getSequence());
            contentSet.add(dictInfo.getContent());
            if (dictInfo.getIsDefault()){
                defaultCount ++;
            }
        }
        // 如果存到set里会小于 那么就有重复顺序
        if (sequenceSet.size() < dictInfoList.size()){
            return RespBean.error("排列序号不能重复");
        }
        //判断默认值是否唯一
        if (defaultCount > 1){
            return RespBean.error("默认值只能唯一");
        }
        // 判断字典项是否唯一
        if (contentSet.size() < dictInfoList.size()){
            return RespBean.error("字典项内容不能重复");
        }
        System.out.println("数据正确");
        // 数据正确  那就去更新
        if (dictMapper.updateDictAndDictInfo(dict,dictInfoList)){
            return RespBean.success("更新字典成功");
        }
        return RespBean.error("更新字典失败");
    }



    @Override
    @Transactional
    public RespBean addDict(DictVo dictVo) {
        // 如果字典信息插入不成功，就直接return
        Dict dict = dictVo.getDict();
        List<DictInfo> dictInfoList = dictVo.getDictInfoList();
        // 判断是否已存在
        Dict exits = dictMapper.selectOne(new QueryWrapper<Dict>().eq("tagZh", dict.getTagZh()).or().eq("tag", dict.getTag()));
        if (exits!=null) return RespBean.error("添加字典失败，中英文标识不能重复");
        // 判断排序是否正确  默认值是否唯一  字典项是否唯一
        Set<Integer> sequenceSet = new HashSet<>();
        Set<String> contentSet = new HashSet<>();
        Integer defaultCount = 0;
        for (DictInfo dictInfo : dictInfoList) {
            sequenceSet.add(dictInfo.getSequence());
            contentSet.add(dictInfo.getContent());
            if (dictInfo.getIsDefault()){
                defaultCount ++;
            }
        }
        // 如果存到set里会小于 那么就有重复顺序
        if (sequenceSet.size() < dictInfoList.size()){
            return RespBean.error("排列序号有重复");
        }
        //判断默认值是否唯一
        if (defaultCount > 1){
            return RespBean.error("默认值只能唯一");
        }
        // 判断字典项是否唯一
        if (contentSet.size() < dictInfoList.size()){
            return RespBean.error("字典项内容不能重复");
        }
        if (dictMapper.insertDictAndDictInfo(dict,dictInfoList)) {
            return RespBean.success("添加字典成功");
        }
        return RespBean.success("添加字典失败");
    }


}
