package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.module.afterschool.domain.dto.EduclGradeQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.EduclGradeEntity;
import com.sxkj.edu.module.afterschool.domain.vo.EduclGradeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 年级Mapper接口
 *
 * @author system
 * @date 2026-05-17
 */
@Mapper
@Component
public interface EduclGradeMapper extends BaseMapper<EduclGradeEntity> {

    /**
     * 年级分页关联查询（含学校名称）
     */
    Page<EduclGradeVO> pageList(Page<EduclGradeVO> page, @Param("query") EduclGradeQueryAO query);

    String getGradeNameById(@Param("classId") Long classId, @Param("schoolId") Long schoolId);
}
