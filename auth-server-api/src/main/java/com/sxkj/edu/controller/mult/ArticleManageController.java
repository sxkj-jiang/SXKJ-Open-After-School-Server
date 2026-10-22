package com.sxkj.edu.controller.mult;

import com.sxkj.edu.common.anno.NoNeedLogin;
import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.domain.mult.ao.*;
import com.sxkj.edu.domain.mult.vo.PortalArticleCategoryVO;
import com.sxkj.edu.domain.mult.vo.TPortalArticleManageDetailVO;
import com.sxkj.edu.domain.mult.vo.TPortalArticleManageVO;
import com.sxkj.edu.domain.mult.vo.TPortalArticleTypeVO;
import com.sxkj.edu.domain.sys.entity.mult.TPortalArticleType;
import com.sxkj.edu.service.TPortalArticleManageService;
import com.sxkj.edu.service.TPortalArticleTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/portal-article")
@Api(tags = {SwaggerTagConst.MULT.MULT_ARTICLE})
@OperateLog
public class ArticleManageController {

    @Resource
    TPortalArticleManageService articleManageService;

    @Resource
    TPortalArticleTypeService articleTypeService;


    @PostMapping("/articleType/upt-ins")
    @ApiOperation("文章管理-类型管理-更新/新增")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-类型管理-更新/新增", businessType = "文章管理-类型管理")
    @RolePermission(roles = {1,4})
    public JsonResult<String> articleTypeUpdateOrIns(@RequestBody @Valid TPortalArticleTypeAO update) {
        return articleTypeService.articleTypeUpdateOrIns(update);
    }

    @GetMapping("/articleType/detail/{id}")
    @ApiOperation("文章管理-类型管理-详情")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-类型管理-详情", businessType = "文章管理-类型管理")
    public JsonResult<TPortalArticleType> detailArticleType(@PathVariable("id") Long id) {
        return JsonResult.buildSuccess(articleTypeService.getById(id));
    }

    @PostMapping("/articleType/delete/{id}")
    @ApiOperation("文章管理-类型管理-删除")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-类型管理-删除", businessType = "文章管理-类型管理")
    @RolePermission(roles = {1,4})
    public JsonResult<String> deleteArticleType(@PathVariable("id") Long id) {
        return articleTypeService.deleteArticleTypeInfo(id);
    }

    @PostMapping("/articleType/page")
    @ApiOperation("文章管理-类型管理-分页查询")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-类型管理-分页查询", businessType = "文章管理-类型管理")
    public JsonResult<PageVO<TPortalArticleTypeVO>> articleTypePageQuery(@RequestBody TPortalArticleTypePageAO query) {
        return JsonResult.buildSuccess(articleTypeService.queryArticleTypePage(query));
    }

    @GetMapping("/articleType/list")
    @ApiOperation("文章管理-类型管理-不分页查询")
    @NoNeedLogin
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-类型管理-不分页查询", businessType = "文章管理-类型管理")
    public JsonResult<List<TPortalArticleTypeVO>> articleTypeListQuery() {
        return JsonResult.buildSuccess(articleTypeService.queryArticleTypeList());
    }

    @GetMapping("/articleType/app/list")
    @ApiOperation("文章管理-类型管理-小程序")
    @NoNeedLogin
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-类型管理-小程序", businessType = "文章管理-类型管理")
    public JsonResult<List<TPortalArticleTypeVO>> articleTypeAppListQuery() {
        return JsonResult.buildSuccess(articleTypeService.articleTypeAppListQuery());
    }

    @PostMapping("/articleType/enable")
    @ApiOperation("文章管理-类型管理-启用/禁用")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-类型管理-启用/禁用", businessType = "文章管理-类型管理")
    @RolePermission(roles = {1,4})
    public JsonResult<String> articleTypeEnableOrDis(@RequestBody @Valid TPortalArticleTypeUptDisableAo ao) {
        return articleTypeService.articleTypeEnableOrDis(ao);
    }

    @PostMapping("/articleManage/upt-ins")
    @ApiOperation("文章管理-更新/新增")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-更新/新增", businessType = "文章管理")
    @RolePermission(roles = {1,4})
    public JsonResult<String> articleManageUpdateOrIns(@RequestBody @Valid TPortalArticleManageAO update) {
        return articleManageService.articleManageUpdateOrIns(update);
    }

    @PostMapping("/articleManage/carousel/upt-ins")
    @ApiOperation("文章管理-轮播图公告-更新/新增")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-轮播图公告-更新/新增", businessType = "文章管理")
    @RolePermission(roles = {1,4})
    public JsonResult<String> articleManageCarouselUpdateOrIns(@RequestBody @Valid ArticleManageCarouselAO update) {
        return articleManageService.articleManageCarouselUpdateOrIns(update);
    }

    @GetMapping("/articleManage/detail/{id}")
    @ApiOperation("文章管理-详情")
    @NoNeedLogin
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-详情", businessType = "文章管理")
    public JsonResult<TPortalArticleManageDetailVO> detailArticleManage(@PathVariable("id") Long id) {
        return JsonResult.buildSuccess(articleManageService.detailArticleManage(id));
    }

    @PostMapping("/articleManage/delete/{id}")
    @ApiOperation("文章管理-删除")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-删除", businessType = "文章管理")
    @RolePermission(roles = {1,4})
    public JsonResult<String> deleteArticleManage(@PathVariable("id") Long id) {
        return articleManageService.deleteArticleManage(id);
    }

    @PostMapping("/articleManage/page")
    @ApiOperation("文章管理-分页查询")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-分页查询", businessType = "文章管理")
    public JsonResult<PageVO<TPortalArticleManageVO>> articleManagePageQuery(@RequestBody TPortalArticleManagePageAO query) {
        return JsonResult.buildSuccess(articleManageService.queryArticleManagePage(query));
    }

    @PostMapping("/articleManage/export")
    @ApiOperation("文章管理-导出")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-导出", businessType = "文章管理")
    public void exportArticleManage(@RequestBody TPortalArticleManagePageAO query, HttpServletResponse response) {
        articleManageService.exportArticleManage(query, response);
    }

    @PostMapping("/articleManage/isShow")
    @ApiOperation("文章管理-是否展示")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-是否展示", businessType = "文章管理")
    @RolePermission(roles = {1,4})
    public JsonResult<String> articleManageeIsShow(@RequestBody @Valid TPortalArticleManageShowAo ao) {
        return articleManageService.articleManageeIsShow(ao);
    }

    @PostMapping("/articleManage/mobile")
    @ApiOperation("文章管理-移动端-分类展示")
    @NoNeedLogin
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-移动端-分类展示", businessType = "文章管理")
    public JsonResult<List<PortalArticleCategoryVO>> articleManageeMobile(@RequestBody PortalArticleManageListAo query) {
        return articleManageService.articleManageeMobile(query);
    }

    @PostMapping("/articleManage/mobile/more")
    @ApiOperation("文章管理-移动端-分类下分页查询")
    @NoNeedLogin
    @OperateLog(type = AmqpConstant.OPERATION, value = "文章管理-移动端-分类下分页查询", businessType = "文章管理")
    public JsonResult<PageVO<TPortalArticleManageVO>> articleManageeMobileMore(@RequestBody @Valid PortalArticleManageAo manageAo) {
        return JsonResult.buildSuccess(articleManageService.articleManageeMobileMore(manageAo));
    }

}
