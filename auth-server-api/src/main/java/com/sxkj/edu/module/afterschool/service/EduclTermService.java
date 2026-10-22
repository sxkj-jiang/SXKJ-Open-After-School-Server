package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.dto.EduclTermAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.EduclTermQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.domain.vo.EduclCalendarEventVO;
import com.sxkj.edu.module.afterschool.domain.vo.EduclTermVO;
import com.sxkj.edu.module.afterschool.domain.vo.TermWeekMonthDropdownVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 学期 Service 接口
 *
 * @author system
 * @date 2026-05-19
 */
public interface EduclTermService extends IService<EduclTermEntity> {

    /**
     * 分页查询
     */
    PageResultDTO<EduclTermVO> queryEduclTermPage(EduclTermQueryAO qo);

    /**
     * 新增
     */
    JsonResult<String> addEduclTerm(EduclTermAddAO ao);

    /**
     * 编辑
     */
    JsonResult<String> updateEduclTerm(EduclTermAddAO ao);

    /**
     * 逻辑删除
     */
    JsonResult<String> deleteEduclTerm(Long id);

    /**
     * 根据ID查询详情（含校历事件列表）
     */
    JsonResult<EduclTermVO> getEduclTermDetail(Long id);

    /**
     * 导入
     */
    JsonResult<String> importEduclTerm(Long schoolId, MultipartFile file);

    /**
     * 导出
     */
    void exportEduclTerm(EduclTermQueryAO qo, HttpServletResponse response);

    // 获取学期类型编码
    Integer getTermTypeById(Long id);

    /**
     * 查询指定日期区间内的公历节假日
     */
    List<EduclCalendarEventVO> queryHolidayEvents(Date startDate, Date endDate);

    /**
     * 获取当前学期周数/月份下拉选项
     */
    List<TermWeekMonthDropdownVO> getCurrentTermWeekMonthDropdown();

    // 判断学期是否存在
    Long getEduclTermExistForId(String termName,Long schoolId);

    /**
     * 根据学校id获取当前学期
     */
    EduclTermEntity getCurrentTermBySchoolId(Long schoolId);
}
