package com.sxkj.edu.module.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.domain.sys.entity.TBaseExamineRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 审核记录表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2022-12-11
 */
public interface BaseExamRecordMapper extends BaseMapper<TBaseExamineRecordEntity> {

    List<Long> getIdsByExam(@Param("examStatus") Integer examStatus, @Param("code") String code);

}
