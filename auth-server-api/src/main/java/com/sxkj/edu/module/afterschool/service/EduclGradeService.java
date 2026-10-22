package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.domain.vo.khfw.EduclGradeSimpleVO;
import com.sxkj.edu.module.afterschool.domain.ao.EduclGradeListQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.EduclGradeAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.EduclGradeQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.EduclGradeEntity;
import com.sxkj.edu.module.afterschool.domain.vo.EduclGradeVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 年级 Service 接口
 *
 * @author system
 * @date 2026-05-19
 */
public interface EduclGradeService extends IService<EduclGradeEntity> {

    /**
     * 分页查询
     */
    PageResultDTO<EduclGradeVO> queryEduclGradePage(EduclGradeQueryAO qo);

    /**
     * 根据ID查询详情
     */
    JsonResult<EduclGradeVO> getEduclGradeDetail(Long id);

    /**
     * 新增
     */
    JsonResult<String> addEduclGrade(EduclGradeAddAO ao);

    /**
     * 编辑
     */
    JsonResult<String> updateEduclGrade(EduclGradeAddAO ao);

    /**
     * 逻辑删除
     */
    JsonResult<String> deleteEduclGrade(Long id);

    /**
     * 导入
     */
    JsonResult<String> importEduclGrade( MultipartFile file);

    /**
     * 导出
     */
    void exportEduclGrade(EduclGradeQueryAO qo, HttpServletResponse response);

    // 学生导入时自动创建年级、班级
    Long createEduclGrade(Long schoolId,Integer schoolStage,String gradeName);

    // 根据班级查询年级名称
    String getGradeNameByClassId(Long classId,Long schoolId);

    /**
     * 年级列表查询
     */
    List<EduclGradeVO> queryGradeList(EduclGradeQueryAO qo);

    // 根据年级ID集合返回id和名称
    Map<Long,String> queryGradeIdAndNameByIds(List<Long> ids);

    /**
     * 根据学校ID查询年级列表（最大返回10条）
     */
    List<EduclGradeSimpleVO> queryGradeSimpleList(EduclGradeListQueryAO qo);
}
