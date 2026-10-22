package com.sxkj.edu.common.base.domain;

import com.sxkj.edu.common.base.enums.DataScopeTypeEnum;
import com.sxkj.edu.common.base.enums.DataScopeWhereInTypeEnum;
import lombok.Data;


@Data
public class DataScopeSqlConfigDTO {

    /**
     * 数据范围类型
     * {@link DataScopeTypeEnum}
     */
    private DataScopeTypeEnum dataScopeType;

    /**
     * join sql 具体实现类
     */
    private Class joinSqlImplClazz;

    private String joinSql;

    private Integer whereIndex;

    /**
     * whereIn类型
     * {@link DataScopeWhereInTypeEnum}
     */
    private DataScopeWhereInTypeEnum dataScopeWhereInType;
}
