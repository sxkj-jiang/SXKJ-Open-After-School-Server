package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsTeacherSubjectEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsSubjectVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Mapper
@Component
public interface AsTeacherSubjectMapper extends BaseMapper<AsTeacherSubjectEntity> {

    /**
     * 根据教师ID查询教学科目
     *
     * @param teacherId 教师ID
     * @return 学科列表（含年级名称、学科名称）
     */
    List<AsSubjectVO> listSubjectByTeacherId(@Param("teacherId") Long teacherId);

    /**
     * 批量查询教师教学科目（含teacherId用于分组）
     *
     * @param teacherIds 教师ID集合
     * @return 学科列表（含teacherId字段）
     */
    List<AsSubjectVO> listSubjectsByTeacherIds(@Param("teacherIds") Collection<Long> teacherIds);
}
