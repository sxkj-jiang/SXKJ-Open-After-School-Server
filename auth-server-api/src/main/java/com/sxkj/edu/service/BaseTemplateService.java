package com.sxkj.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.domain.mult.ao.BaseTemplateChildQueryAO;
import com.sxkj.edu.domain.mult.ao.BaseTemplatePageCodeQueryAO;
import com.sxkj.edu.domain.mult.ao.BaseTemplateQueryAO;
import com.sxkj.edu.domain.mult.ao.BaseTemplateUpdateAO;
import com.sxkj.edu.domain.mult.vo.BaseTemplatePageCodeVO;
import com.sxkj.edu.domain.mult.vo.BaseTemplateVO;
import com.sxkj.edu.domain.mult.vo.LabelVO;
import com.sxkj.edu.module.business.entity.BaseTemplatePo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BaseTemplateService extends IService<BaseTemplatePo> {
    PageVO<BaseTemplateVO> pageQuery(BaseTemplateQueryAO ao);

    /**
     * 根据页面code查询启用模板列表(供页面下载导入模板使用)
     */
    List<BaseTemplatePageCodeVO> queryByPageCode(BaseTemplatePageCodeQueryAO ao);

    void exportExcel(HttpServletResponse response, List<Long> ids);

    List<LabelVO> menuList();

    JsonResult<BaseTemplateVO> queryByMenu(BaseTemplateChildQueryAO ao);

    //文章类型新增或编辑
    JsonResult<String> saveOrUpdateBaseTemplate(BaseTemplateUpdateAO ao);
}
