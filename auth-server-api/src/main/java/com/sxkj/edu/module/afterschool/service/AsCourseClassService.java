package com.sxkj.edu.module.afterschool.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.ao.*;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseClassCountVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseClassDropdownVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseClassPageVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseClassSessionDetailVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseClassVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsSessionAttendancePageVO;

/**
 * 课程班Service实现类
 *
 * @author system
 * @date 2026-05-17
 */
public interface AsCourseClassService extends IService<AsCourseClassEntity> {


    /**
     * 根据ID查询课程班详情
     */
    AsCourseClassVO getCourseClassDetail(Long id);



    /**
     * 分页查询课程班列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResultDTO<AsCourseClassPageVO> pageQueryCourseClass(AsCourseClassPageQueryAO query);

    /**
     * 统计课程班各报名方式数量
     *
     * @param query 查询条件（schoolId）
     * @return 统计结果
     */
    AsCourseClassCountVO countCourseClass(AsCourseClassCountQueryAO query);

    /**
     * 导出课程班列表（全量导出，内部每批500条）
     *
     * @param query    查询条件（含分页参数）
     * @param response HTTP响应
     */
    void exportCourseClass(AsCourseClassPageQueryAO query, HttpServletResponse response);

    /**
     * 课后服务课程班下拉筛选（当前日期在服务开课结课范围内）
     */
    List<AsCourseClassDropdownVO> listDropdownByServiceDateRange( AsCourseClassDropdownQueryAO ao);

    /**
     * 课程班课次考勤分页查询
     *
     * @param query 查询条件（schoolId + courseClassId）
     * @return 分页结果
     */
    PageResultDTO<AsSessionAttendancePageVO> pageQuerySessionAttendance(AsSessionAttendancePageQueryAO query);



    // 教师课程移交
    JsonResult<String> transferTeacherSubject(AsCourseClassTransferAO ao);
}
