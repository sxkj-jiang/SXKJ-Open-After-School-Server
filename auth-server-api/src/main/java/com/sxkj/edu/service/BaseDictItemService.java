package com.sxkj.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.base.dict.Option;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.domain.ao.QueryChirdDictTypeListAo;
import com.sxkj.edu.domain.base.*;
import com.sxkj.edu.domain.sys.entity.TBaseDictItemEntity;

import java.util.List;
import java.util.Map;

/**
 * 基础数据-字典管理
 *
 * @author jiangjj
 */
public interface BaseDictItemService extends IService<TBaseDictItemEntity> {

    // 分页查询字典
    JsonResult<PageVO<DictItemVo>> listDictItemPages(QueryDictTypeAo queryParams);

    JsonResult<String> saveDictItem(SaveBaseDictItemAo dictItemForm);

    JsonResult<String> updateDictItem(UpdateBaseDictItemAo dictItemForm);

    JsonResult<String> disableDictItem(DisableDictItemAo disableDictItemAo);

    // 删除字段
    JsonResult<String> deleteBaseDictItem(DeleteDictItemAo deleteDictItemAo);

    List<Option> listDictItemsByTypeCode(String typeCode);

    TBaseDictItemEntity getDictTypeFormDataByCode(String typeCode);

    // 查询所有父类字典
    JsonResult<List<DictItemVo>> getParentDictList(QueryDictTypeAo queryDictTypeAo);

    // 查询当前父类下的子类字典
    JsonResult<List<DictItemVo>> getChirdDictList(QueryChirdDictTypeAo chirdDictTypeAo);

    // 查询当前父类下的子类字典，兼容多个字典查询
    JsonResult<Map<String, DictItemVo>> getChirdDictMoreList(QueryChirdDictTypeListAo chirdDictTypeAo);


    // 查询当前字典是否有子类字典
    List<DictItemChildVo> getChirdDictListById(Long currId);

    // 根据typecode获取字典名称
    String getDictTypeDataByCodeForName(String typeCode);

    Map<String, Long> getGraduationSchool(List<String> codes);
}
