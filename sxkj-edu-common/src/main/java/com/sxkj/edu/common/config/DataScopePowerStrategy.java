package com.sxkj.edu.common.config;


import com.sxkj.edu.common.base.domain.DataScopeSqlConfigDTO;
import com.sxkj.edu.common.base.enums.DataScopeViewTypeEnum;

/**
 *@Description : 数据范围策略 ,使用DataScopeWhereInTypeEnum.CUSTOM_STRATEGY类型，DataScope注解的joinSql属性无用
 *@Author : JIANGJJ
 *@Date : 2022-9-7 15:04
 *@Company : SXKJ
 *@Version : 1.0
 **/
public abstract class DataScopePowerStrategy {

    /**
     * 获取joinsql 字符串
     * @param viewTypeEnum 查看的类型
     * @param sqlConfigDTO
     * @return
     */
    public abstract String getCondition(DataScopeViewTypeEnum viewTypeEnum, DataScopeSqlConfigDTO sqlConfigDTO);
}
