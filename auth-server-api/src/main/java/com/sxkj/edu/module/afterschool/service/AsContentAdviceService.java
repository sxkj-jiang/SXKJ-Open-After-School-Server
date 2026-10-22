package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.dto.AsContentAdviceAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsContentAdviceAuditAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsContentAdviceQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsContentAdviceEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsContentAdviceVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 意见反馈 Service 接口
 *
 * @author system
 * @date 2026-06-20
 */
public interface AsContentAdviceService extends IService<AsContentAdviceEntity> {

    /**
     * 分页查询
     */
    PageResultDTO<AsContentAdviceVO> queryAsContentAdvicePage(AsContentAdviceQueryAO qo);

    /**
     * 新增
     */
    JsonResult<String> addAsContentAdvice(AsContentAdviceAddAO ao);

    /**
     * 编辑
     */
    JsonResult<String> updateAsContentAdvice(AsContentAdviceAddAO ao);

    /**
     * 审核
     */
    JsonResult<String> auditAsContentAdvice(AsContentAdviceAuditAO ao);

    /**
     * 逻辑删除
     */
    JsonResult<String> deleteAsContentAdvice(Long id);

    /**
     * 导出
     */
    void exportAsContentAdvice(AsContentAdviceQueryAO qo, HttpServletResponse response);

    /**
     * 查询详情
     */
    JsonResult<AsContentAdviceVO> getAsContentAdviceDetail(Long id);
}
