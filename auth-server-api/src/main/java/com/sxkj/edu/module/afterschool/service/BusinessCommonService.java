package com.sxkj.edu.module.afterschool.service;

import com.sxkj.edu.module.afterschool.domain.vo.SchoolCalendarVO;

/**
 * 通用业务 Service 接口
 *
 * @author system
 * @date 2026-06-14
 */
public interface BusinessCommonService {

    /**
     * 查询当前学校的校历信息
     * <p>
     * 根据当前时间与 t_as_service 的开课开始/结束时间匹配，
     * 计算学期总周数、当前周次以及目标周下的日历集合（含节假日信息）。
     *
     * @param schoolId 学校ID（必填）
     * @param weekNo   第几周（选填，为空时取当前周次）
     * @return 校历信息，若当前时间未命中任何课后服务则返回 null
     */
    SchoolCalendarVO querySchoolCalendar(Long schoolId, Integer weekNo);
}
