package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseCategoryAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseCategoryListQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseCategoryQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseCategoryEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseCategoryVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * 课后类别 Service 接口
 *
 * @author system
 * @date 2026-05-19
 */
public interface AsCourseCategoryService extends IService<AsCourseCategoryEntity> {

    /**
     * 分页查询
     */
    PageResultDTO<AsCourseCategoryVO> queryAsCourseCategoryPage(AsCourseCategoryQueryAO qo);

    /**
     * 新增
     */
    JsonResult<String> addAsCourseCategory(AsCourseCategoryAddAO ao);

    /**
     * 编辑
     */
    JsonResult<String> updateAsCourseCategory(AsCourseCategoryAddAO ao);

    /**
     * 逻辑删除
     */
    JsonResult<String> deleteAsCourseCategory(Long id);

    /**
     * 导入
     */
    JsonResult<String> importAsCourseCategory(Long schoolId, MultipartFile file);

    /**
     * 导出
     */
    void exportAsCourseCategory(AsCourseCategoryQueryAO qo, HttpServletResponse response);

    /**
     * 启用/禁用
     */
    JsonResult<String> updateEnabledStatus(Long id, Integer enabledStatus);

    /**
     * 查询详情
     */
    JsonResult<AsCourseCategoryVO> getAsCourseCategoryDetail(Long id);

    /**
     * 条件查询下拉列表（仅返回启用状态）
     */
    JsonResult<List<AsCourseCategoryVO>> listDropDown(AsCourseCategoryListQueryAO qo);

    /**
     * 查询所有类别树
     */
    JsonResult<List<AsCourseCategoryVO>> listCategoryTree(AsCourseCategoryQueryAO qo);
}
