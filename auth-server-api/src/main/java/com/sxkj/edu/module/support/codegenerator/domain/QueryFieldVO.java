package com.sxkj.edu.module.support.codegenerator.domain;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class QueryFieldVO {

    private String fieldName;

    private String columnName;

    private String columnDesc;

    private String fieldType;

    private String sqlOperate;

}
