package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.afterschool.domain.dto.EduclClassQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.EduclClassVO;
import com.sxkj.edu.module.afterschool.domain.vo.TeacherNoticeStudentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 班级Mapper接口 (afterschool模块)
 *
 * @author system
 * @date 2026-05-19
 */
@Mapper
@Component
public interface EduclClassMapper extends BaseMapper<TEduclClass> {

    /**
     * 班级分页关联查询（含学校、年级、班主任名称）
     */
    Page<EduclClassVO> pageList(Page<EduclClassVO> page, @Param("query") EduclClassQueryAO query);

    /**
     * 班级详情关联查询（含学校、年级、班主任名称）
     */
    EduclClassVO selectDetail(@Param("id") Long id);

    /**
     * 查询指定类别与指定课程班下的班主任
     */
    List<Long> getHeadTeachersByCourse(@Param("categoryIdList")List<Long> categoryIdList, @Param("courseClassIdList")List<Long> courseClassIdList);

    /**
     * 根据班级ID查询学生信息
     */
    List<TeacherNoticeStudentVO> queryStudentsByClassIds(
            @Param("schoolId") Long schoolId,
            @Param("termId") Long termId,
            @Param("noticeObjIdList") List<Long> noticeObjIdList,
            @Param("studentIds") List<Long> studentIds);
}
