package com.sxkj.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.enums.ExamTypeEnum;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.base.record.TBaseExamRecordAo;
import com.sxkj.edu.domain.base.record.TBaseExamRecordVO;
import com.sxkj.edu.domain.sys.entity.TBaseExamineRecordEntity;
import com.sxkj.edu.module.mapper.base.BaseExamRecordMapper;
import com.sxkj.edu.service.TBaseExamRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description : 审核-操作记录管理
 * @Author : JIANGJJ
 * @Date : 2022-12-26 20:47
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Slf4j
@Service
public class TBaseExamRecordServiceImpl extends ServiceImpl<BaseExamRecordMapper, TBaseExamineRecordEntity> implements TBaseExamRecordService {

    @Resource
    BaseExamRecordMapper recordMapper;

    @Override
    public Integer getLastetExamStatus(Long originId, String examType) {
        LambdaQueryWrapper<TBaseExamineRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TBaseExamineRecordEntity::getExamStatus);
        wrapper.eq(TBaseExamineRecordEntity::getOriginId, originId);
        wrapper.eq(TBaseExamineRecordEntity::getExamType, examType);
        wrapper.orderByDesc(TBaseExamineRecordEntity::getId);
        wrapper.last("limit 1");
        TBaseExamineRecordEntity tBaseExamineRecord = this.getOne(wrapper);
        Integer status = 5;
        if (ObjectUtil.isNotNull(tBaseExamineRecord)) {
            if (StrUtil.isNotBlank(tBaseExamineRecord.getExamAdvice())) {
                status = tBaseExamineRecord.getExamStatus();
            }
        }
        return status;
    }

    @Override
    public List<Long> getIds(Integer examStatus, String examType) {
        return recordMapper.getIdsByExam(examStatus, examType);
    }

    @Override
    public JsonResult<List<TBaseExamRecordVO>> queryBaseExamRecord(String examType, Long originId) {
        LambdaQueryWrapper<TBaseExamineRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TBaseExamineRecordEntity::getIsValid,1);
        if(StringUtils.isNotBlank(examType)){
            wrapper.eq(TBaseExamineRecordEntity::getExamType, examType);
        }
        wrapper.eq(TBaseExamineRecordEntity::getOriginId, originId);
        wrapper.orderByDesc(TBaseExamineRecordEntity::getExamDate);
        wrapper.last("limit 20");
        List<TBaseExamineRecordEntity> recordList = this.list(wrapper);
        List<TBaseExamRecordVO> voList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(recordList)) {
            recordList.forEach(s -> {
                TBaseExamRecordVO recordVO = new TBaseExamRecordVO();
                BeanUtil.copyProperties(s, recordVO);
                voList.add(recordVO);
            });
        }
        return JsonResult.buildSuccess(voList);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void addBaseExamRecord(TBaseExamineRecordEntity record) {
        this.save(record);
    }

    @Override
    public void addBaseExamineRecord(Long id, String examType, Integer status, String advice) {
        TBaseExamineRecordEntity tBaseExamineRecord = new TBaseExamineRecordEntity();
        tBaseExamineRecord.setExamUser(SmartRequestTokenUtil.getRequestUser().getEmployeeBO().getActualName());
        tBaseExamineRecord.setExamDate(new Date());
        tBaseExamineRecord.setOriginId(id);
        tBaseExamineRecord.setExamType(examType);
        tBaseExamineRecord.setExamStatus(status);
        if (StrUtil.isNotBlank(advice)) {
            tBaseExamineRecord.setExamAdvice(advice);
        }
        this.save(tBaseExamineRecord);
    }

    @Override
    public String getLastetAdvice(Long id) {
        LambdaQueryWrapper<TBaseExamineRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TBaseExamineRecordEntity::getExamStatus);
        wrapper.eq(TBaseExamineRecordEntity::getOriginId, id);
        wrapper.orderByDesc(TBaseExamineRecordEntity::getId);
        wrapper.last("limit 1");
        TBaseExamineRecordEntity tBaseExamineRecord = this.getOne(wrapper);
        String advice = "无";
        if (ObjectUtil.isNotNull(tBaseExamineRecord)) {
            if (StrUtil.isNotBlank(tBaseExamineRecord.getExamAdvice())) {
                advice = tBaseExamineRecord.getExamAdvice();
            }
        }
        return advice;
    }

    @Override
    public Integer getRecordExamStatus(Long id) {
        LambdaQueryWrapper<TBaseExamineRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TBaseExamineRecordEntity::getExamStatus);
        wrapper.eq(TBaseExamineRecordEntity::getExamType, ExamTypeEnum.studentRecord.getCode());
        wrapper.eq(TBaseExamineRecordEntity::getOriginId, id);
        wrapper.orderByDesc(TBaseExamineRecordEntity::getId);
        wrapper.last("limit 1");
        TBaseExamineRecordEntity tBaseExamineRecord = this.getOne(wrapper);
        return ObjectUtil.isNotNull(tBaseExamineRecord) ? tBaseExamineRecord.getExamStatus() : 0;
    }

    @Override
    public List<Long> getRecordIds(Integer examStatus) {
        return recordMapper.getIdsByExam(examStatus, ExamTypeEnum.studentRecord.getCode());
    }

    @Override
    public void addBaseExamRecord(TBaseExamRecordAo recordAo, String examCode) {
        TBaseExamineRecordEntity tBaseExamineRecord = new TBaseExamineRecordEntity();
        BeanUtil.copyProperties(recordAo, tBaseExamineRecord);
        tBaseExamineRecord.setIsValid(1);
        tBaseExamineRecord.setExamType(examCode);
        this.addBaseExamRecord(tBaseExamineRecord);
    }
}
