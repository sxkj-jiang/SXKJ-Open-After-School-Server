package com.sxkj.edu.controller.mult;

import com.sxkj.edu.common.anno.NoNeedLogin;
import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.domain.ao.BaseExamRecordQueryAo;
import com.sxkj.edu.domain.ao.TPortalInstructionsDisableUptAo;
import com.sxkj.edu.domain.base.down.TDownloadCenterAo;
import com.sxkj.edu.domain.base.down.TDownloadReq;
import com.sxkj.edu.domain.base.down.TDownloadUptAo;
import com.sxkj.edu.domain.base.mult.*;
import com.sxkj.edu.domain.base.record.TBaseExamRecordVO;
import com.sxkj.edu.domain.vo.TDownloadCenterQueryVo;
import com.sxkj.edu.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/portal-manager")
@Api(tags = {SwaggerTagConst.Admin.MULT_MANAGER})
@OperateLog
public class MultController {

    @Resource
    private TPortalCarouselService carouselService;


    @Resource
    private TBaseExamRecordService baseExamRecordService;


    @PostMapping("/carousel/page/list")
    @ApiOperation("轮播图-分页查询 PC/移动端")
    @NoNeedLogin
    public JsonResult<PageVO<TPortalCarouselVO>> carouselPageList(@RequestBody TPortalCarouselAo query) {
        return JsonResult.buildSuccess(carouselService.queryPortalCarouselList(query));
    }

    @PostMapping("/carousel/page/detail")
    @ApiOperation("轮播图-详情")
    @NoNeedLogin
    public JsonResult<TPortalCarouselVO> queryCarouselDetail(@RequestBody TPortalCarouselQueryDetailAo query) {
        return carouselService.queryPortalCarouselDetail(query);
    }


    @PostMapping("/carousel/upt-ins")
    @ApiOperation("轮播图-更新/新增")
    @OperateLog(type = AmqpConstant.OPERATION, value = "轮播图-更新/新增", businessType = "轮播图")
    @RolePermission(roles = {1,4})
    public JsonResult<String> carouselUpdateOrIns(@RequestBody @Valid TPortalCarouselUptAo update) {
        CheckParamUtil.checkObjectParam(update.getCulUrl(), "请输入图片地址");
        CheckParamUtil.checkObjectParam(update.getStatus(), "请输入轮播图状态");
        return carouselService.updateCarouselInfo(update);
    }

    @PostMapping("/carousel/delete/{id}")
    @ApiOperation("轮播图-删除")
    @OperateLog(type = AmqpConstant.OPERATION, value = "轮播图-删除", businessType = "轮播图")
    @RolePermission(roles = {1,4})
    public JsonResult<String> deleteCarousel(@PathVariable("id") Long id) {
        return carouselService.deleteCarouselInfo(id);
    }

    @PostMapping("/queryBaseExamLog")
    @ApiOperation("审核记录-查询")
    @OperateLog(type = AmqpConstant.OPERATION, value = "审核记录-查询", businessType = "审核记录")
    @RolePermission(roles = {1,4})
    public JsonResult<List<TBaseExamRecordVO>> queryBaseExamLogApi(@RequestBody @Valid BaseExamRecordQueryAo ao) {
        return baseExamRecordService.queryBaseExamRecord(ao.getExamType(),ao.getOriginId());
    }

}
