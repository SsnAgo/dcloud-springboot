package com.example.dcloud.service;

import com.example.dcloud.pojo.RespBean;
import com.example.dcloud.pojo.Sign;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dcloud.pojo.SignRecord;
import com.example.dcloud.vo.SignHistoryVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ssn
 * @since 2021-03-27
 */
public interface ISignService extends IService<Sign> {

    /**
     * 学生进行无限制签到
     * @param signId
     * @param sid
     * @return
     */
    RespBean noLimitSign(Integer signId, Integer sid,String local);

    /**
     * 学生进行一分钟签到
     * @param signId
     * @param sid
     * @return
     */
    RespBean timeLimitSign(Integer signId, Integer sid,String local);

    /**
     * 学生进行手势签到
     * @param cid
     * @param sid
     * @param sequence
     * @return
     */
//    RespBean handSign(Integer cid, Integer sid, String sequence);

    /**
     * 教师关闭签到
     * @param cid
     * @param type
     * @return
     */
    RespBean closeSign(Integer cid, Integer type);

    /**
     * 查看签到情况
     * @param id
     * @return
     */
    RespBean signInfo(Integer id);

    /**
     * 事实查看签到人数
     * @param id
     * @return
     */
    RespBean showCount(Integer id);

    /**
     * 获取班课的签到历史
     * @param cid
     * @return
     */
    List<SignHistoryVo> getCourseHistory(Integer cid);

    /**
     * 获取在该班课该学生的签到记录
     * @param cid
     * @param sid
     * @return
     */
    List<SignRecord> getStudentHistory(Integer cid, Integer sid);
}
