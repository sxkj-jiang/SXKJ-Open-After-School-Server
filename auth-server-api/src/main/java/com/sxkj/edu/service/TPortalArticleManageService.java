package com.sxkj.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.domain.mult.ao.*;
import com.sxkj.edu.domain.mult.vo.PortalArticleCategoryVO;
import com.sxkj.edu.domain.mult.vo.TPortalArticleManageDetailVO;
import com.sxkj.edu.domain.mult.vo.TPortalArticleManageVO;
import com.sxkj.edu.domain.sys.entity.mult.TPortalArticleManage;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface TPortalArticleManageService extends IService<TPortalArticleManage> {

    //文章新增或编辑
    JsonResult<String> articleManageUpdateOrIns(TPortalArticleManageAO update);

    //文章详情
    TPortalArticleManageDetailVO detailArticleManage(Long id);

    //删除文章
    JsonResult<String> deleteArticleManage(Long id);

    //文章分页查询
    PageVO<TPortalArticleManageVO> queryArticleManagePage(TPortalArticleManagePageAO query);

    //文章导出
    void exportArticleManage(TPortalArticleManagePageAO query, HttpServletResponse response);

    //文章是否展示功能
    JsonResult<String> articleManageeIsShow(TPortalArticleManageShowAo ao);

    //移动端-文章分类接口
    JsonResult<List<PortalArticleCategoryVO>> articleManageeMobile(PortalArticleManageListAo query);

    //移动端-文章分类接口-查看更多
   PageVO<TPortalArticleManageVO> articleManageeMobileMore(PortalArticleManageAo manageAo);

   //轮播图的公告新增/编辑
    JsonResult<String> articleManageCarouselUpdateOrIns( ArticleManageCarouselAO update);
}
