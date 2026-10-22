package com.sxkj.edu.controller;


import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.domain.ao.QueryChirdDictTypeListAo;
import com.sxkj.edu.domain.base.*;
import com.sxkj.edu.service.BaseDictItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * @Description : 字典管理优化
 * @Author : JIANGJJ
 * @Date : 2024-10-12 12:10
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Api(tags = {SwaggerTagConst.Admin.MANAGER_DICT})
@RestController
@RequestMapping("/api/base/dict/items")
@RequiredArgsConstructor
@OperateLog
public class BaseDictItemController {

    private final BaseDictItemService dictItemService;

    @ApiOperation(value = "数据字典管理-分页查询")
    @PostMapping("/pages")
    @OperateLog(type = AmqpConstant.OPERATION, value = "数据字典管理-分页查询", businessType = "数据字典管理")
    public JsonResult<PageVO<DictItemVo>> listDictItemPages(@RequestBody QueryDictTypeAo queryDictTypeAo) {
        return dictItemService.listDictItemPages(queryDictTypeAo);
    }


    @ApiOperation(value = "数据字典管理-保存")
    @PostMapping("/add")
    @OperateLog(type = AmqpConstant.OPERATION, value = "数据字典管理-保存", businessType = "数据字典管理")
    public JsonResult saveDictItem(@RequestBody SaveBaseDictItemAo itemAo) {
        return dictItemService.saveDictItem(itemAo);
    }

    @ApiOperation(value = "数据字典管理-修改")
    @PostMapping("/upt")
    @OperateLog(type = AmqpConstant.OPERATION, value = "数据字典管理-修改", businessType = "数据字典管理")
    public JsonResult<String> updateDictItem(@RequestBody UpdateBaseDictItemAo dictItemForm) {
        return dictItemService.updateDictItem(dictItemForm);
    }

    @ApiOperation(value = "数据字典管理-启用禁用")
    @PostMapping("/disable")
    @OperateLog(type = AmqpConstant.OPERATION, value = "数据字典管理-启用禁用", businessType = "数据字典管理")
    public JsonResult<String> disBaseDictItems(@RequestBody DisableDictItemAo disableDictItemAo) {
        return dictItemService.disableDictItem(disableDictItemAo);
    }

    @ApiOperation(value = "数据字典管理-删除")
    @PostMapping("/del")
    @OperateLog(type = AmqpConstant.OPERATION, value = "数据字典管理-删除", businessType = "数据字典管理")
    public JsonResult<String> delBaseDictItems(@RequestBody DeleteDictItemAo disableDictItemAo) {
        return dictItemService.deleteBaseDictItem(disableDictItemAo);
    }

    @ApiOperation(value = "查询所有父类字典-下拉检索", notes = "jiangjj")
    @PostMapping("/getParentDict")
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询所有父类字典-下列检索", businessType = "数据字典管理")
    public JsonResult<List<DictItemVo>> getParentDictList(@RequestBody QueryDictTypeAo queryDictTypeAo) {
        return dictItemService.getParentDictList(queryDictTypeAo);
    }

    @ApiOperation(value = "查询当前父类下的子类字典")
    @PostMapping("/getChirdDict")
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询当前父类下的子类字典", businessType = "数据字典管理")
    public JsonResult<List<DictItemVo>> getChirdDictList(@RequestBody QueryChirdDictTypeAo chirdDictTypeAo) {
        return dictItemService.getChirdDictList(chirdDictTypeAo);
    }

    @ApiOperation(value = "批量查询父类下的子类字典")
    @PostMapping("/getChirdDict/list")
    @OperateLog(type = AmqpConstant.OPERATION, value = "批量查询父类下的子类字典", businessType = "数据字典管理")
    public JsonResult<Map<String, DictItemVo>> getChirdDictMoreList(@RequestBody @Valid QueryChirdDictTypeListAo chirdDictTypeAo) {
        return dictItemService.getChirdDictMoreList(chirdDictTypeAo);
    }
}
