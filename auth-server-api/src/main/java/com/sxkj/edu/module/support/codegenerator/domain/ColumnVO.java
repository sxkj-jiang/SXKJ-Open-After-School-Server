package com.sxkj.edu.module.support.codegenerator.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnVO {

    private String columnName;

    private String columnType;

    private String columnDesc;

    private String fieldType;

    private String fieldName;

    private Boolean isNumber;

}
