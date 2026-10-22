package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsSchedulePeriodEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AsSchedulePeriodMapper extends BaseMapper<AsSchedulePeriodEntity> {

    /**
     * 检查同学校下其他已启用的课后时间方案是否与当前方案的年级有交集
     * @param schoolId 学校ID
     * @param excludeTemplateId 排除的方案ID
     * @param gradeIds 当前方案的年级ID集合
     * @return 冲突的方案数量
     */
    int countOverlappingEnabledPeriods(@Param("schoolId") Long schoolId,
                                       @Param("excludeTemplateId") Long excludeTemplateId,
                                       @Param("gradeIds") List<Long> gradeIds);
}
