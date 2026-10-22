package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.domain.vo.khfw.EduclClassSimpleVO;
import com.sxkj.edu.module.afterschool.domain.ao.EduclClassListQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.EduclClassAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.EduclClassQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.EduclClassVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 班级 Service 接口
 *
 * @author system
 * @date 2026-05-19
 */
public interface EduclClassService extends IService<TEduclClass> {

    /**
     * 分页查询
     */
    PageResultDTO<EduclClassVO> queryEduclClassPage(EduclClassQueryAO qo);

    /**
     * 新增
     */
    JsonResult<String> addEduclClass(EduclClassAddAO ao);

    /**
     * 编辑
     */
    JsonResult<String> updateEduclClass(EduclClassAddAO ao);

    /**
     * 逻辑删除
     */
    JsonResult<String> deleteEduclClass(Long id);

    /**
     * 根据ID查询详情
     */
    JsonResult<EduclClassVO> getEduclClassDetail(Long id);

    /**
     * 导入
     */
    JsonResult<String> importEduclClass(MultipartFile file);

    /**
     * 导出
     */
    void exportEduclClass(EduclClassQueryAO qo, HttpServletResponse response);

    // 学生导入时创建班级
    Long createStudentClassImport(Long schoolId,Long gradeId, String className,Long termId);

    // 根据班级ID集合返回id和名称
    Map<Long,String> queryClassIdAndNameByIds(List<Long> ids);

    // 导入时更新班级的教师班主任id
    void updateClassTeacherId(Long classId, Long teacherId,Long schoolId);

    /**
     * 根据学校ID、年级编号查询班级列表（最大返回50条）
     */
    List<EduclClassSimpleVO> queryClassSimpleList(EduclClassListQueryAO qo);
}
