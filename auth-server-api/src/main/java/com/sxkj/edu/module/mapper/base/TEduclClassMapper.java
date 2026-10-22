package com.sxkj.edu.module.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherStudentDestinationClassVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TEduclClassMapper extends BaseMapper<TEduclClass> {

    /**
     * 通过学期与班主任教师id查询班级信息与班级人数
     */
    List<AsTeacherStudentDestinationClassVO> listHeadTeacherClassWithStudentCount(
            @Param("headTeacherId") Long headTeacherId,
            @Param("schoolId") Long schoolId,
            @Param("termId") Long termId,
            @Param("classId") Long classId);

}
