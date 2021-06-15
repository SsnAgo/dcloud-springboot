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
import com.example.dcloud.dto.DictDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 服务实现类
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
    public RespPageBean listDict(Integer currentPage, Integer size, String search) {
        Page<Dict> page = new Page<>(currentPage, size);
        IPage<Dict> dictPage = dictMapper.listDictPage(page, search);
        RespPageBean respPageBean = new RespPageBean(dictPage.getTotal(), dictPage.getRecords());
        return respPageBean;
    }

    @Override
    @Transactional
    public RespBean updateDict(DictDto dictDto) {
        // 如果字典信息插入不成功，就直接return
        Dict dict = dictDto.getDict();
        List<DictInfo> dictInfoList = dictDto.getDictInfoList();

        // 判断当前是否有该字典 如果没有则不能修改
        Dict exits = dictMapper.selectById(dict.getId());
        if (null == exits) {
            return RespBean.error("要修改的字典不存在");
        }
//        // tag和tagZh 除了不变 否则不能和其他的重复
//        exits = dictMapper.selectOne(new QueryWrapper<Dict>().eq("tag", dict.getTag()).ne("id", dict.getId()));
//        if (exits != null) {
//            return RespBean.error("该英文标识已存在");
//        }
//        exits = dictMapper.selectOne(new QueryWrapper<Dict>().eq("tagZh",dict.getTagZh()).ne("id",dict.getId()));
//        if (exits != null){
//            return RespBean.error("该中文标识已存在");
//        }
        // 要修改的该tag存在，就判断中文标识是否存在 先查当前要改的字典在数据库中对应的中文标识
        String currentTagZh = exits.getTagZh();
        String currentTag = exits.getTag();
        // 如果当前的不和目前的相同 那么要去判断有没有存在  有则不能改
        if (!currentTag.equals(dict.getTag())) {
            // 查询数据库中有没有除了当前tag之外的 还有tagZh相同的项
            exits = dictMapper.selectOne(new QueryWrapper<Dict>().eq("tag", dict.getTagZh()).ne("id", dict.getId()));
            // 如果有其他的 就不能改
            if (exits != null) {
                return RespBean.error("更新字典失败，英文标识不能重复");
            }
            // 否则 就改掉这个tag
            else{
            }
        }
        if (!currentTagZh.equals(dict.getTagZh())) {
            // 查询数据库中有没有除了当前tag之外的 还有tagZh相同的项
            exits = dictMapper.selectOne(new QueryWrapper<Dict>().eq("tagZh", dict.getTagZh()).ne("id", dict.getId()));
            if (exits != null) {
                return RespBean.error("更新字典失败，中文标识不能重复");
            }
        }
        // 上述两项验证后 就先更新字典
        dictMapper.updateById(dict);
        // 查出最新的字典信息
        exits = dictMapper.selectById(dict.getId());
        System.out.println("开始更新字典项了");
        // 接下来更新字典项
        if (dictInfoList == null) {
            return RespBean.success("更新成功");
        }
        // 判断要更新的有没有值 没有就表示要删掉
        if (dictInfoList.size() == 0) {
            dictInfoMapper.deleteDictInfo(exits.getTag());
            return RespBean.success("更新成功");
        }

        // 判断排序是否正确  默认值是否唯一 以及内容是否不重复
        Set<Integer> sequenceSet = new HashSet<>();
        Set<String> contentSet = new HashSet<>();
        Integer defaultCount = 0;
        for (DictInfo dictInfo : dictInfoList) {
            sequenceSet.add(dictInfo.getSequence());
            contentSet.add(dictInfo.getContent());
            if (dictInfo.getIsDefault()) {
                defaultCount++;
            }
        }
        // 如果存到set里会小于 那么就有重复顺序
        if (sequenceSet.size() < dictInfoList.size()) {
            return RespBean.error("排列序号不能重复");
        }
        //判断默认值是否唯一
        if (defaultCount > 1) {
            return RespBean.error("默认值只能唯一");
        }
        if (defaultCount == 0) {
            return RespBean.error("必须设置一个默认值");
        }
        // 判断字典项是否唯一
        if (contentSet.size() < dictInfoList.size()) {
            return RespBean.error("字典项内容不能重复");
        }
        System.out.println("数据正确");
        // 数据正确  那就去更新

        // 更新逻辑要修改

        // 先去数据库查询原来的数据字典信息
        List<DictInfo> origin = dictInfoMapper.selectList(new QueryWrapper<DictInfo>().eq("tag", exits.getTag()));
        for (DictInfo i : dictInfoList) {
            // 有id说明是更改的
            if (i.getId() != null) {
                dictInfoMapper.updateById(i);
            }
            // 没有id 如果内容和之前不一样说明是新增的  如果内容和之前一样 那么不新增  去修改
            else {
//                boolean isUpdate;
                int size = origin.size();
                int count = 0;
                // 判断该i的content在不在原来里面出现
                for (DictInfo o : origin) {
                    // 如果没id 但是内容一样的 可以认为是修改 将这个id赋予它 并修改
                    if (o.getContent().equals(i.getContent())) {
                        i.setId(o.getId());
                        dictInfoMapper.updateById(i);
                    } else {
                        count++;
                    }
                }
                // 遍历完了都没有找到content相等的 就表示这个是新增的
                if (count == size) {
                    dictInfoMapper.insert(i);
                }

            }
        }
        // 遍历原来的表  如果原来的表里的id在现在的没有就表示删除了
        int size = dictInfoList.size();
        for (DictInfo o : origin) {
            int count = 0;
            for (DictInfo curr : dictInfoList) {
                if (curr.getId() != null && !o.getId().equals(curr.getId())) {
                    count++;
                }
            }
            // 表示没找到 就删除这个o对应的id
            if (count == size) {
                System.out.println("删除id对应为" + o.getId());
                dictInfoMapper.deleteById(o.getId());
            }
        }
        return RespBean.success("更新成功");
    }


    @Override
    @Transactional
    public RespBean addDict(DictDto dictDto) {
        // 如果字典信息插入不成功，就直接return
        Dict dict = dictDto.getDict();
        List<DictInfo> dictInfoList = dictDto.getDictInfoList();
        // 判断是否已存在
        Dict exits = dictMapper.selectOne(new QueryWrapper<Dict>().eq("tagZh", dict.getTagZh()).or().eq("tag", dict.getTag()));
        if (exits != null) return RespBean.error("添加字典失败，中英文标识不能重复");
        // 判断排序是否正确  默认值是否唯一  字典项是否唯一
        Set<Integer> sequenceSet = new HashSet<>();
        Set<String> contentSet = new HashSet<>();
        Integer defaultCount = 0;
        for (DictInfo dictInfo : dictInfoList) {
            sequenceSet.add(dictInfo.getSequence());
            contentSet.add(dictInfo.getContent());
            if (dictInfo.getIsDefault()) {
                defaultCount++;
            }
        }
//        // 如果存到set里会小于 那么就有重复顺序
//        if (sequenceSet.size() < dictInfoList.size()) {
//            return RespBean.error("排列序号有重复");
//        }
        //判断默认值是否唯一
        if (defaultCount > 1) {
            return RespBean.error("默认值只能唯一");
        }
        // 判断字典项是否唯一
        if (contentSet.size() < dictInfoList.size()) {
            return RespBean.error("字典项内容不能重复");
        }
        if (dictMapper.insertDictAndDictInfo(dict, dictInfoList)) {
            return RespBean.success("添加字典成功");
        }
        return RespBean.success("添加字典失败");
    }


    @Override
    @Transactional
    public RespBean deleteDict(Integer id) {
        Dict dict = dictMapper.selectById(id);
        if (dict == null) {
            return RespBean.error("该字典不存在");
        }
        String tag = dict.getTag();
        Integer res = dictInfoMapper.deleteDictInfo(tag);
        if (res == 0) {
            System.out.println("该字典没有字典项");
        }
        Integer ans = dictMapper.deleteById(id);
        if (ans != 1) {
            return RespBean.error("删除字典失败");
        }
        return RespBean.success("删除字典成功");
    }




/**
 * {
 * 	"dict": {
 * 		"description": "测试删除字典",
 * 		"tag": "ttt",
 * 		"tagZh": "测试删除"
 *        },
 * 	"dictInfoList": [
 *        {
 * 			"content": "测试删除1",
 * 			"isDefault": true,
 * 			"sequence": 0,
 * 			"tag": "ttt"
 *        },
 * {
 * 			"content": "测试删除2",
 * 			"isDefault": false,
 * 			"sequence": 2,
 * 			"tag": "ttt"
 *        }
 * 	]
 * }
 */
}
