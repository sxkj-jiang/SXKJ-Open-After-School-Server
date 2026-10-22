package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;

import java.util.List;
import com.sxkj.edu.module.afterschool.domain.dto.AsScheduleTemplateListQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsScheduleTemplateQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.KhfwAsScheduleTemplateAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.ZxglAsScheduleTemplateAddAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsScheduleTemplateEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsScheduleTemplateListVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsScheduleTemplateVO;
import com.sxkj.edu.module.afterschool.domain.vo.KhfwAsScheduleTemplateDetailVO;
import com.sxkj.edu.module.afterschool.domain.vo.ZxglAsScheduleTemplateDetailVO;
import org.springframework.web.multipart.MultipartFile;

public interface AsScheduleTemplateService extends IService<AsScheduleTemplateEntity> {

    /**
     * 分页查询
     */
    PageResultDTO<AsScheduleTemplateVO> queryScheduleTemplatePage(AsScheduleTemplateQueryAO qo);
    /**
     * 添加
     */
    JsonResult<String> addZxglScheduleTemplate(ZxglAsScheduleTemplateAddAO ao);
    /**
     * 修改
     */
    JsonResult<String> updateZxglScheduleTemplate(ZxglAsScheduleTemplateAddAO ao);
    /**
     * 作息管理/课后时间管理-删除
     */
    JsonResult<String> deleteScheduleTemplate( Long id);
    /**
     * 作息管理-获取详情
     */
    JsonResult<ZxglAsScheduleTemplateDetailVO> getZxglScheduleTemplateDetail(Long id);

    /**
     * 作息管理-复制方案
     */
    JsonResult<String> copyZxglScheduleTemplate(Long id);

    /**
     * 作息管理-获取启用作息
     */
    JsonResult<ZxglAsScheduleTemplateDetailVO> getZxglScheduleTemplateEnableDetail(Long schoolId);
    /**
     * 作息管理/课后时间管理-更新启用禁用状态
     */
    JsonResult<String> updateEnabledStatus(Long id, Integer enabledStatus);

    /**
     * 方案下拉列表查询
     */
    List<AsScheduleTemplateListVO> queryScheduleTemplateList(AsScheduleTemplateListQueryAO qo);

    // =============================== 课后时间管理 ===============================

    /**
     * 课后时间管理-新增
     */
    JsonResult<String> addKhfwScheduleTemplate(KhfwAsScheduleTemplateAddAO ao);

    /**
     * 课后时间管理-编辑
     */
    JsonResult<String> updateKhfwScheduleTemplate(KhfwAsScheduleTemplateAddAO ao);

    /**
     * 课后时间管理-查询详情
     */
    JsonResult<KhfwAsScheduleTemplateDetailVO> getKhfwScheduleTemplateDetail(Long id);

    /**
     * 课后时间管理-批量导入
     */
    JsonResult<String> importKhfwScheduleTemplate(MultipartFile file);

}
