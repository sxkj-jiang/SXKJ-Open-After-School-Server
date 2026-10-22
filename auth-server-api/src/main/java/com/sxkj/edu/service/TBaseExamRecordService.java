package com.sxkj.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.domain.base.record.TBaseExamRecordAo;
import com.sxkj.edu.domain.base.record.TBaseExamRecordVO;
import com.sxkj.edu.domain.sys.entity.TBaseExamineRecordEntity;

import java.util.List;

/**
 * 公共审核记录实现
 */
public interface TBaseExamRecordService extends IService<TBaseExamineRecordEntity> {

    //获取审核状态
    Integer getLastetExamStatus(Long originId, String examType);

    //获取满足审核状态id集合
    List<Long> getIds(Integer examStatus, String examType);

    JsonResult<List<TBaseExamRecordVO>> queryBaseExamRecord(String examType, Long originId);

    void addBaseExamRecord(TBaseExamineRecordEntity record);

    //添加审核记录(1-已审核，2-待审核，3-已拒绝，4-撤回)
    void addBaseExamineRecord(Long id, String examType, Integer status, String advice);

    //获取最新审核意见
    String getLastetAdvice(Long id);

    //获取报名登记表审核状态
    Integer getRecordExamStatus(Long id);

    //获取满足审核状态的报名id集合
    List<Long> getRecordIds(Integer examStatus);

    void addBaseExamRecord(TBaseExamRecordAo recordAo, String examCode);
}
