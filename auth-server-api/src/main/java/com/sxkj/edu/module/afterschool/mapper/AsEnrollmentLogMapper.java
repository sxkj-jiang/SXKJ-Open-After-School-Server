package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsEnrollmentLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 学生选课操作日志Mapper接口
 *
 * @author system
 * @date 2026-05-20
 */
@Mapper
@Component
public interface AsEnrollmentLogMapper extends BaseMapper<AsEnrollmentLogEntity> {
}
