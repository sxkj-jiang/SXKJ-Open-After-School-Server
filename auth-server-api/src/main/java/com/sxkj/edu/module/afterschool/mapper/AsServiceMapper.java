package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.module.afterschool.domain.ao.CourseSchedulePageQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.CourseScheduleListDTO;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import com.sxkj.edu.module.afterschool.domain.vo.CourseSchedulePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 课后服务活动Mapper接口
 *
 * @author system
 * @date 2026-05-17
 */
@Mapper
@Component
public interface AsServiceMapper extends BaseMapper<AsServiceEntity> {
    /**
     * 锁定课后服务，防止复制过程中源配置并发变化
     */
    AsServiceEntity selectByIdForUpdate(@Param("id") Long id);

    /**
     * 课表管理-分页查询
     */
    Page<CourseSchedulePageVO> queryCourseSchedulePage(Page<Object> objectPage, @Param("query") CourseSchedulePageQueryAO qo);

    /**
     * 课表管理-查询服务下所有课次数据（t_as_service - t_as_course_class - t_as_course_session）
     */
    List<CourseScheduleListDTO> queryCourseSessionList(@Param("serviceId")Long serviceId);

    /**
     * 根据学校和学期查询该学期下所有课程班ID
     */
    List<AsCourseClassEntity> queryCourseClassIdsByTerm(@Param("schoolId") Long schoolId, @Param("termId") Long termId);
}
