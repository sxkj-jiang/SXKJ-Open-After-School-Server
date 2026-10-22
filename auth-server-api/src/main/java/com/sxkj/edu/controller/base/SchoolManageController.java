package com.sxkj.edu.controller.base;

import cn.hutool.core.util.StrUtil;
import com.sxkj.edu.common.anno.NoNeedLogin;
import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.domain.ao.SchoolManageCreateAO;
import com.sxkj.edu.domain.ao.SchoolManageQueryAO;
import com.sxkj.edu.domain.ao.SchoolManageSwithAO;
import com.sxkj.edu.domain.ao.SchoolManageUpdateAO;
import com.sxkj.edu.domain.base.SchoolManageSimpleVO;
import com.sxkj.edu.domain.base.SchoolManageVO;
import com.sxkj.edu.domain.dto.SchoolManageDTO;
import com.sxkj.edu.domain.vo.FileVO;
import com.sxkj.edu.module.business.entity.SchoolManagePo;
import com.sxkj.edu.service.BaseFileService;
import com.sxkj.edu.service.SchoolManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 学校管理 前端控制器
 * </p>
 *
 * @author jiangjj
 * @since 2026-05-12
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.SCHOOL_MANAGE})
@RequestMapping("/school/manage")
@OperateLog(type = AmqpConstant.OPERATION, value = "学校管理", businessType = "学校管理")
public class SchoolManageController {

    @Autowired
    private SchoolManageService schoolManageService;
    @Resource
    private BaseFileService baseFileService;

    @PostMapping("/list")
    @ApiOperation(value = "学校管理-查询", notes = "jiangjj")
    @NoNeedLogin
    @OperateLog(type = AmqpConstant.OPERATION, value = "学校管理-查询", businessType = "学校管理")
    public JsonResult<List<SchoolManageSimpleVO>> querySchoolManagerPage(@RequestBody SchoolManageQueryAO ao) {
        List<SchoolManageDTO> page = schoolManageService.listSchoolManage(SchoolManageQueryAO.convert(ao));
        return JsonResult.buildSuccess(SchoolManageSimpleVO.convert(page));
    }

    @PostMapping("/pageQuery")
    @ApiOperation(value = "学校管理-分页查询", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学校管理-分页查询", businessType = "学校管理")
    public JsonResult<PageVO<SchoolManageVO>> pageSchoolManagerQuery(@RequestBody SchoolManageQueryAO ao) {
        PageVO<SchoolManageDTO> page = schoolManageService.pageQuery(SchoolManageQueryAO.convert(ao), ao.getPageIndex(), ao.getPageSize());
        return JsonResult.buildSuccess(new PageVO<>(page.getTotal(), page.getPages(), SchoolManageVO.convert(page.getList())));
    }

    @PostMapping("/create")
    @ApiOperation(value = "学校管理-创建", notes = "jiangjj")
    @RolePermission(roles = {1})
    @OperateLog(type = AmqpConstant.OPERATION, value = "学校管理-创建", businessType = "学校管理")
    public JsonResult<String> createSchoolManager(@Valid @RequestBody SchoolManageCreateAO ao) {
        return schoolManageService.saveSchoolManage(SchoolManageCreateAO.convert(ao));
    }

    @PostMapping("/update")
    @ApiOperation(value = "学校管理-更新", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "更新", businessType = "学校管理")
    @RolePermission(roles = {1})
    public JsonResult<String> updateSchoolManager(@RequestBody SchoolManageUpdateAO ao) {
        return schoolManageService.updateSchoolManage(SchoolManageUpdateAO.convert(ao));
    }

    @GetMapping("/detail/{id}")
    @ApiOperation(value = "学校管理-详情", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学校管理-详情", businessType = "学校管理")
    public JsonResult<SchoolManageVO> queryDetailSchoolManager(@PathVariable String id) {
        SchoolManagePo projectRecruitManagePO = schoolManageService.getById(id);
        SchoolManageVO po = new SchoolManageVO();
        BeanUtils.copyProperties(projectRecruitManagePO, po);
        if (StrUtil.isNotEmpty(projectRecruitManagePO.getSchoolLogo())) {
            Map<Long, FileVO> fileIdUrlMap = baseFileService.getFileById(Arrays.stream(projectRecruitManagePO.getSchoolLogo().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            List<FileVO> fileVOS = new ArrayList<>(fileIdUrlMap.values());
            po.setFileUrlList(fileVOS);
        }
        return JsonResult.buildSuccess(po);
    }

    @PostMapping("/swithSchool/data")
    @ApiOperation(value = "超管用户-切换学校", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "超管用户-切换学校", businessType = "学校管理")
    @RolePermission(roles = {1})
    public JsonResult swithDataSchool(@RequestBody @Valid SchoolManageSwithAO ao, HttpServletRequest request) {
        return schoolManageService.swithSchoolData(ao,request);
    }

}
