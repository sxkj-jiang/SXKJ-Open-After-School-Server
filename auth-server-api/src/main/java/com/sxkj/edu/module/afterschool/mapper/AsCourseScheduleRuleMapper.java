package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseScheduleRuleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 课程班排课规则Mapper接口。
 *
 * @author system
 * @date 2026-05-23
 */
@Mapper
@Component
public interface AsCourseScheduleRuleMapper extends BaseMapper<AsCourseScheduleRuleEntity> {
}
