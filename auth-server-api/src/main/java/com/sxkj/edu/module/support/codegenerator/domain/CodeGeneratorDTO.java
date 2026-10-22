package com.sxkj.edu.module.support.codegenerator.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CodeGeneratorDTO {

    /**
     * 需要生成代码的表名
     */
    private String tableName;

    /**
     * 表前缀
     */
    private String tablePrefix;

    /**
     * 基础包路径
     */
    private String basePackage;

    /**
     * module下的子包
     */
    private String modulePackage;

    /**
     * 公司
     */
    private String company;

    /**
     * 作者
     */
    private String author;

    /**
     * 需要构建查询方法的列
     */
    private List<CodeGeneratorQueryColumnDTO> queryColumnList;
}
