package com.sxkj.edu.module.system.datascope.service;

import com.google.common.collect.Lists;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.enums.DataScopeTypeEnum;
import com.sxkj.edu.common.base.enums.DataScopeViewTypeEnum;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.module.system.datascope.DataScopeRoleDao;
import com.sxkj.edu.module.system.datascope.domain.dto.*;
import com.sxkj.edu.module.system.datascope.domain.entity.DataScopeRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;


@Service
public class DataScopeService {

    @Autowired
    private DataScopeRoleDao dataScopeRoleDao;

    /**
     * 获取所有可以进行数据范围配置的信息
     *
     * @return
     */
    public JsonResult<List<DataScopeAndViewTypeVO>> dataScopeList() {
        List<DataScopeDTO> dataScopeList = this.getDataScopeType();
        List<DataScopeAndViewTypeVO> dataScopeAndTypeList = SmartBeanUtil.copyList(dataScopeList, DataScopeAndViewTypeVO.class);
        List<DataScopeViewTypeVO> typeList = this.getViewType();
        dataScopeAndTypeList.forEach(e -> {
            e.setViewTypeList(typeList);
        });
        return JsonResult.buildSuccess(dataScopeAndTypeList);
    }

    /**
     * 获取当前系统存在的数据可见范围
     *
     * @return
     */
    public List<DataScopeViewTypeVO> getViewType() {
        List<DataScopeViewTypeVO> viewTypeList = Lists.newArrayList();
        DataScopeViewTypeEnum[] enums = DataScopeViewTypeEnum.class.getEnumConstants();
        DataScopeViewTypeVO dataScopeViewTypeDTO;
        for (DataScopeViewTypeEnum viewTypeEnum : enums) {
            dataScopeViewTypeDTO = DataScopeViewTypeVO.builder().viewType(viewTypeEnum.getValue()).viewTypeLevel(viewTypeEnum.getLevel()).viewTypeName(viewTypeEnum.getDesc()).build();
            viewTypeList.add(dataScopeViewTypeDTO);
        }
        Comparator<DataScopeViewTypeVO> comparator = (h1, h2) -> h1.getViewTypeLevel().compareTo(h2.getViewTypeLevel());
        viewTypeList.sort(comparator);
        return viewTypeList;
    }

    public List<DataScopeDTO> getDataScopeType() {
        List<DataScopeDTO> dataScopeTypeList = Lists.newArrayList();
        DataScopeTypeEnum[] enums = DataScopeTypeEnum.class.getEnumConstants();
        DataScopeDTO dataScopeDTO;
        for (DataScopeTypeEnum typeEnum : enums) {
            dataScopeDTO =
                    DataScopeDTO.builder().dataScopeType(typeEnum.getValue()).dataScopeTypeDesc(typeEnum.getDesc()).dataScopeTypeName(typeEnum.getName()).dataScopeTypeSort(typeEnum.getSort()).build();
            dataScopeTypeList.add(dataScopeDTO);
        }
        Comparator<DataScopeDTO> comparator = (h1, h2) -> h1.getDataScopeTypeSort().compareTo(h2.getDataScopeTypeSort());
        dataScopeTypeList.sort(comparator);
        return dataScopeTypeList;
    }

    /**
     * 获取某个角色的数据范围设置信息
     *
     * @param roleId
     * @return
     */
    public JsonResult<List<DataScopeSelectVO>> dataScopeListByRole(Long roleId) {

        List<DataScopeRoleEntity> dataScopeRoleEntityList = dataScopeRoleDao.listByRoleId(roleId);
        if (CollectionUtils.isEmpty(dataScopeRoleEntityList)) {
            return JsonResult.buildSuccess(Lists.newArrayList());
        }
        List<DataScopeSelectVO> dataScopeSelects = SmartBeanUtil.copyList(dataScopeRoleEntityList, DataScopeSelectVO.class);
        return JsonResult.buildSuccess(dataScopeSelects);
    }

    /**
     * 批量设置某个角色的数据范围设置信息
     *
     * @param batchSetRoleDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> dataScopeBatchSet(DataScopeBatchSetRoleDTO batchSetRoleDTO) {
        List<DataScopeBatchSetDTO> batchSetList = batchSetRoleDTO.getBatchSetList();
        if (CollectionUtils.isEmpty(batchSetList)) {
            return JsonResult.buildError(ResultCode.ERROR_PARAM.code(), "缺少配置信息");
        }
        List<DataScopeRoleEntity> dataScopeRoleEntityList = SmartBeanUtil.copyList(batchSetList, DataScopeRoleEntity.class);
        dataScopeRoleEntityList.forEach(e -> e.setRoleId(batchSetRoleDTO.getRoleId()));
        dataScopeRoleDao.deleteByRoleId(batchSetRoleDTO.getRoleId());
        dataScopeRoleDao.batchInsert(dataScopeRoleEntityList);
        return JsonResult.buildSuccess();
    }

}
