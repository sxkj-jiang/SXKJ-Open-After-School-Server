package com.sxkj.edu.common.anno;


import com.sxkj.edu.common.base.enums.DataScopeTypeEnum;
import com.sxkj.edu.common.base.enums.DataScopeWhereInTypeEnum;
import com.sxkj.edu.common.config.DataScopePowerStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *@Description : 数据范围
 *@Author : JIANGJJ
 *@Date : 2022-9-6 15:48
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataScope {

    DataScopeTypeEnum dataScopeType() default DataScopeTypeEnum.DEFAULT;

    DataScopeWhereInTypeEnum whereInType() default DataScopeWhereInTypeEnum.EMPLOYEE;

    /**
     * DataScopeWhereInTypeEnum.CUSTOM_STRATEGY类型 才可使用joinSqlImplClazz属性
     * @return
     */
    Class<? extends DataScopePowerStrategy> joinSqlImplClazz()  default DataScopePowerStrategy.class;

    /**
     *
     * 第几个where 条件 从0开始
     * @return
     */
    int whereIndex() default 0;

    /**
     * DataScopeWhereInTypeEnum为CUSTOM_STRATEGY类型时，此属性无效
     * @return
     */
    String joinSql() default "";

}
