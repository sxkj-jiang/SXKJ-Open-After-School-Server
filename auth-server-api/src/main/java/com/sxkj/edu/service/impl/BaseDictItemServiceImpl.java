package com.sxkj.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.dict.Option;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.common.utils.Args;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.ao.QueryChirdDictTypeListAo;
import com.sxkj.edu.domain.base.*;
import com.sxkj.edu.domain.sys.entity.TBaseDictItemEntity;
import com.sxkj.edu.module.mapper.sys.BaseDictItemMapper;
import com.sxkj.edu.service.BaseDictItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据字典项业务实现类
 *
 * @author jiangjj
 */
@Service
@Slf4j
public class BaseDictItemServiceImpl extends ServiceImpl<BaseDictItemMapper, TBaseDictItemEntity> implements BaseDictItemService {

    @Resource
    private BaseDictItemMapper baseDictItemMapper;

    private Boolean checkExistEntenry(String typeName, String typeCode) {
        Boolean exist = false;
        LambdaQueryWrapper<TBaseDictItemEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TBaseDictItemEntity::getTypeName, typeName);
        queryWrapper.eq(TBaseDictItemEntity::getTypeCode, typeCode);
        queryWrapper.eq(TBaseDictItemEntity::getStatus, 1);
        List<TBaseDictItemEntity> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            exist = true;
        }
        return exist;
    }

    @Override
    public JsonResult<PageVO<DictItemVo>> listDictItemPages(QueryDictTypeAo queryParams) {
        JsonResult<PageVO<DictItemVo>> jsonResult = JsonResult.buildError(ResultCode.NOT_EXISTS.code(), "字典查无数据");
        try {
            Page<TBaseDictItemEntity> page = new Page<>(queryParams.getPageIndex(), queryParams.getPageSize());
            LambdaQueryWrapper<TBaseDictItemEntity> wrapper = new LambdaQueryWrapper<TBaseDictItemEntity>()
                    .like(Args.isNotEmpty(queryParams.getTypeName()), TBaseDictItemEntity::getTypeName, queryParams.getTypeName())
                    .eq(TBaseDictItemEntity::getIsValid, 1)
                    .eq(Args.isNotEmpty(queryParams.getTypeCode()), TBaseDictItemEntity::getTypeCode, queryParams.getTypeCode())
                    .orderByDesc(TBaseDictItemEntity::getTypeCode,TBaseDictItemEntity::getSort);
            baseMapper.selectPage(page, wrapper);
            List<TBaseDictItemEntity> records = page.getRecords();
            List<DictItemVo> dictItemVos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(records)) {
                records.forEach(f -> {
                    DictItemVo itemVo = new DictItemVo();
                    BeanUtil.copyProperties(f, itemVo);
                    List<DictItemChildVo> childList = getChirdDictListById(f.getId());
                    itemVo.setChildList(childList);
                    dictItemVos.add(itemVo);
                });
                jsonResult = JsonResult.buildSuccess(new PageVO<>(page.getTotal(), page.getPages(), dictItemVos));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    @Override
    public JsonResult<String> saveDictItem(SaveBaseDictItemAo dictItemForm) {
        JsonResult<String> res = null;
        if (checkExistEntenry(dictItemForm.getTypeName(), dictItemForm.getTypeCode())) {
            res = JsonResult.buildError(ResultCode.DATA_EXIST.code(), "字典名称或编码已存在");
            return res;
        }
        if (null != dictItemForm.getParentId()) {
            TBaseDictItemEntity parentDict = this.getById(dictItemForm.getParentId());
            if (null == parentDict) {
                res = JsonResult.buildError(ResultCode.NOT_EXISTS.code(), "父级字典不存在");
                return res;
            }
        }
        TBaseDictItemEntity origin = new TBaseDictItemEntity();
        BeanUtil.copyProperties(dictItemForm, origin);
        origin.setIsValid(1);
        origin.setStatus(1);
        origin.setCreateTime(new Date());
        origin.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean rower = this.save(origin);
        log.info("call saveDictItem in request,exec result:{}", rower);
        res = JsonResult.buildSuccess("保存成功");
        return res;
    }

    @Override
    public JsonResult<String> updateDictItem(UpdateBaseDictItemAo dictItemForm) {
        JsonResult<String> res = null;
        TBaseDictItemEntity origin = this.getById(dictItemForm.getId());
        if (null == origin) {
            res = JsonResult.buildError(ResultCode.NOT_EXISTS.code(), "字典数据未找到");
            return res;
        }
        if (null != dictItemForm.getParentId()) {
            TBaseDictItemEntity parentDict = this.getById(dictItemForm.getParentId());
            if (null == parentDict) {
                res = JsonResult.buildError(ResultCode.NOT_EXISTS.code(), "父级字典不存在");
                return res;
            }
        }
        BeanUtil.copyProperties(dictItemForm, origin);
        origin.setStatus(dictItemForm.getStatus() == null ? 1 : dictItemForm.getStatus());
        origin.setUpdateTime(new Date());
        origin.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean rower = this.updateById(origin);
        log.info("call updateDictItem in request,exec result:{}", rower);
        res = JsonResult.buildSuccess("更新字典成功");
        return res;
    }

    @Override
    public JsonResult<String> disableDictItem(DisableDictItemAo disableDictItemAo) {
        JsonResult<String> res = null;
        TBaseDictItemEntity origin = this.getById(disableDictItemAo.getId());
        if (null == origin) {
            res = JsonResult.buildError(ResultCode.NOT_EXISTS.code(), "字典数据未找到");
            return res;
        }
        origin.setStatus(disableDictItemAo.getStatus() == null ? 1 : disableDictItemAo.getStatus());
        origin.setUpdateTime(new Date());
        origin.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean rower = this.updateById(origin);
        log.info("call disableDictItem in request,exec result:{}", rower);
        res = JsonResult.buildSuccess("字典启用禁用成功");
        return res;
    }

    @Override
    public JsonResult<String> deleteBaseDictItem(DeleteDictItemAo deleteDictItemAo) {
        JsonResult<String> res = null;
        TBaseDictItemEntity origin = this.getById(deleteDictItemAo.getId());
        if (null == origin) {
            res = JsonResult.buildError(ResultCode.NOT_EXISTS.code(), "字典数据未找到");
            return res;
        }
        origin.setIsValid(0);
        origin.setStatus(0);
        origin.setUpdateTime(new Date());
        origin.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean rower = this.updateById(origin);
        log.info("call deleteBaseDictItem in request,exec result:{}", rower);
        res = JsonResult.buildSuccess("字典删除成功");
        return res;
    }

    @Override
    public List<Option> listDictItemsByTypeCode(String typeCode) {
        // 数据字典项
        List<TBaseDictItemEntity> dictItems = this.list(new LambdaQueryWrapper<TBaseDictItemEntity>()
                .eq(TBaseDictItemEntity::getTypeCode, typeCode)
                .eq(TBaseDictItemEntity::getStatus, 1)
                .select(TBaseDictItemEntity::getTypeName, TBaseDictItemEntity::getTypeCode)
        );

        // 转换下拉数据
        List<Option> options = CollectionUtil.emptyIfNull(dictItems)
                .stream()
                .map(dictItem -> new Option(dictItem.getTypeCode(), dictItem.getTypeName()))
                .collect(Collectors.toList());
        return options;
    }

    @Override
    public TBaseDictItemEntity getDictTypeFormDataByCode(String code) {
        // 获取entity
        TBaseDictItemEntity entity = this.getOne(new LambdaQueryWrapper<TBaseDictItemEntity>()
                .eq(TBaseDictItemEntity::getTypeCode, code)
                .select(
                        TBaseDictItemEntity::getId,
                        TBaseDictItemEntity::getTypeCode,
                        TBaseDictItemEntity::getTypeValue,
                        TBaseDictItemEntity::getTypeName
                ));
        Assert.isTrue(entity != null, "字典类型不存在");
        // 实体转换
        return entity;
    }

    @Override
    public JsonResult<List<DictItemVo>> getParentDictList(QueryDictTypeAo queryDictTypeAo) {
        JsonResult<List<DictItemVo>> jsonResult = null;
        List<DictItemVo> dictItemVoList = new ArrayList<>();
        // 数据字典项
        List<TBaseDictItemEntity> dictItems = this.list(new LambdaQueryWrapper<TBaseDictItemEntity>()
                .eq(Args.isNotEmpty(queryDictTypeAo.getTypeCode()), TBaseDictItemEntity::getTypeCode, queryDictTypeAo.getTypeCode())
                .like(Args.isNotEmpty(queryDictTypeAo.getTypeName()), TBaseDictItemEntity::getTypeName, queryDictTypeAo.getTypeName())
                .eq(TBaseDictItemEntity::getStatus, 1)
                .eq(TBaseDictItemEntity::getIsValid, 1)
                .isNull(TBaseDictItemEntity::getParentId)
        );
        if (CollectionUtils.isNotEmpty(dictItems)) {
            dictItems.forEach(f -> {
                DictItemVo dictItemVo = new DictItemVo();
                BeanUtil.copyProperties(f, dictItemVo);
                dictItemVoList.add(dictItemVo);
            });
            jsonResult = JsonResult.buildSuccess(dictItemVoList);
        } else {
            jsonResult = JsonResult.buildError("字典查无数据");
        }

        return jsonResult;
    }

    @Override
    public JsonResult<List<DictItemVo>> getChirdDictList(QueryChirdDictTypeAo chirdDictTypeAo) {
        JsonResult<List<DictItemVo>> jsonResult = JsonResult.buildError("字典查无数据");
        List<DictItemVo> dictItemVoList = new ArrayList<>();
        if (CheckParamUtil.checkLongIdParam(chirdDictTypeAo.getParentId())) {
            // 数据字典项
            List<TBaseDictItemEntity> dictItems = this.list(new LambdaQueryWrapper<TBaseDictItemEntity>()
                    .eq(TBaseDictItemEntity::getParentId, chirdDictTypeAo.getParentId())
                    .eq(TBaseDictItemEntity::getStatus, 1)
                    .eq(TBaseDictItemEntity::getIsValid, 1)
            );
            if (CollectionUtils.isNotEmpty(dictItems)) {
                dictItems.forEach(f -> {
                    DictItemVo dictItemVo = new DictItemVo();
                    BeanUtil.copyProperties(f, dictItemVo);
                    dictItemVoList.add(dictItemVo);
                });
                jsonResult = JsonResult.buildSuccess(dictItemVoList);
            }
        } else if (StringUtils.isNotEmpty(chirdDictTypeAo.getParentCode())) {
            List<DictItemVo> itemVoList = baseDictItemMapper.queryBaseDictParentByCode(chirdDictTypeAo.getParentCode());
            jsonResult = JsonResult.buildSuccess(itemVoList);
        }

        return jsonResult;
    }

    @Override
    public JsonResult<Map<String, DictItemVo>> getChirdDictMoreList(QueryChirdDictTypeListAo chirdDictTypeAo) {
        JsonResult<Map<String, DictItemVo>> jsonResult = JsonResult.buildError("字典查无数据");
        Map<String, DictItemVo> namePersonMap = new HashMap<>();
        List<DictItemVo> itemVoList = baseDictItemMapper.queryBaseDictParentByList(chirdDictTypeAo);
        if(CollectionUtils.isNotEmpty(itemVoList)){
            namePersonMap = itemVoList.stream()
                    .collect(Collectors.toMap(DictItemVo::getTypeCode, v -> v, (p1, p2) -> p1));

        }
        jsonResult = JsonResult.buildSuccess(namePersonMap);

        return jsonResult;
    }

    @Override
    public List<DictItemChildVo> getChirdDictListById(Long currId) {
        List<DictItemChildVo> dictItemVoList = new ArrayList<>();
        List<TBaseDictItemEntity> dictItems = this.list(new LambdaQueryWrapper<TBaseDictItemEntity>()
                .eq(TBaseDictItemEntity::getParentId, currId)
                .eq(TBaseDictItemEntity::getStatus, 1)
                .eq(TBaseDictItemEntity::getIsValid, 1)
                .orderByAsc(TBaseDictItemEntity::getId)
        );
        if (CollectionUtils.isNotEmpty(dictItems)) {
            dictItems.forEach(f -> {
                DictItemChildVo dictItemVo = new DictItemChildVo();
                BeanUtil.copyProperties(f, dictItemVo);
                dictItemVoList.add(dictItemVo);
            });
        }

        return dictItemVoList;
    }

    @Override
    public String getDictTypeDataByCodeForName(String typeCode) {
        List<TBaseDictItemEntity> dictItems = new ArrayList<>();
        LambdaQueryWrapper<TBaseDictItemEntity> queryWrapper = new LambdaQueryWrapper<TBaseDictItemEntity>()
                .eq(TBaseDictItemEntity::getTypeCode, typeCode)
                .eq(TBaseDictItemEntity::getStatus, 1)
                .eq(TBaseDictItemEntity::getIsValid, 1);
        dictItems = this.list(queryWrapper);
        if(CollectionUtils.isNotEmpty(dictItems)){
            return dictItems.get(0).getTypeName();
        }
        return "";
    }

    @Override
    public Map<String, Long> getGraduationSchool(List<String> codes) {
        List<TBaseDictItemEntity> entities = list(Wrappers.lambdaQuery(TBaseDictItemEntity.class).eq(TBaseDictItemEntity::getTypeCode, "0000"));
        if (entities == null || entities.isEmpty()) return Collections.emptyMap();
        List<TBaseDictItemEntity> subEntities = list(Wrappers.lambdaQuery(TBaseDictItemEntity.class).eq(TBaseDictItemEntity::getParentId, entities.get(0).getId()));
        if (subEntities == null || subEntities.isEmpty()) return Collections.emptyMap();
        return subEntities.stream().collect(Collectors.toMap(TBaseDictItemEntity::getTypeCode, TBaseDictItemEntity::getId));
    }

}




