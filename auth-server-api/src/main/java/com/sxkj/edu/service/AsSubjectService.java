package com.sxkj.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.dto.AsSubjectAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsSubjectQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsSubjectEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsSubjectGradeTreeVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsSubjectVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 学科管理 服务接口
 *
 * @author system
 * @date 2026-08-26
 */
public interface AsSubjectService extends IService<AsSubjectEntity> {

    /**
     * 新增/编辑学科（编码+年级+学校唯一）
     */
    JsonResult<String> addOrUpdateSubject(AsSubjectAddAO ao);

    /**
     * 启用/禁用学科
     */
    JsonResult<String> updateEnabledStatus(Long id, Integer enabled);

    /**
     * 多条件分页查询学科
     */
    PageResultDTO<AsSubjectVO> querySubjectPage(AsSubjectQueryAO qo);

    /**
     * 根据学校ID查询启用学科，按年级分组（树形下拉）
     */
    List<AsSubjectGradeTreeVO> listSubjectTreeBySchoolId(Long schoolId);

    /**
     * 根据ID查询学科详情
     *
     * @param id 学科ID
     * @return 学科详情
     */
    JsonResult<AsSubjectVO> getDetailById(Long id);

    /**
     * 根据ID删除学科（逻辑删除）
     *
     * @param id 学科ID
     * @return 操作结果
     */
    JsonResult<String> deleteSubject(Long id);

    /**
     * 批量导入学科
     *
     * @param schoolId 学校ID
     * @param file     Excel文件
     * @return 操作结果
     */
    JsonResult<String> importSubject(Long schoolId, MultipartFile file);

    /**
     * 根据code查询详情
     * @param subjectCode 学科编号
     * @return
     */
    AsSubjectEntity getDetailByCode(String subjectCode);
}
