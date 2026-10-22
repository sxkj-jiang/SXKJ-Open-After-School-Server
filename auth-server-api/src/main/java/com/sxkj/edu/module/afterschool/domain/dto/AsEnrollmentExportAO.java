package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生选课导出查询对象
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("学生选课导出查询对象")
public class AsEnrollmentExportAO extends AsEnrollmentSelectionPageQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("导出页签：selected已选课组班 unselected未选课组班")
    private String tabType;
}
