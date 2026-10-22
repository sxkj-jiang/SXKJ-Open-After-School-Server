package com.sxkj.edu.module.support.codegenerator.domain;

import com.sxkj.edu.module.support.codegenerator.constant.SqlOperateTypeEnum;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CodeGeneratorQueryColumnDTO {

    /**
     * 生成查询方法的查询列名
     */
    private String columnName;

    /**
     * 此列的查询动作
     */
    private SqlOperateTypeEnum sqlOperate;
}
