package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.domain.ao.AsHeadTeacherClassStudentInfoAO;
import com.sxkj.edu.domain.vo.AsHeadTeacherClassStudentInfoVO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherClassQueryDto;
import com.sxkj.edu.module.afterschool.domain.entity.AsTeacherClassEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AsTeacherClassMapper extends BaseMapper<AsTeacherClassEntity> {


    // 根据教师姓名查询班主任/班级
    AsTeacherClassQueryDto queryTeacherClassByTeacherName(@Param("schoolId") Long schoolId, @Param("classId") Long classId);

    List<AsHeadTeacherClassStudentInfoVO> listClassStudentInfo(AsHeadTeacherClassStudentInfoAO ao);
}
