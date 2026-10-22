package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 学科年级树视图对象（年级为父节点，学科为子节点）
 *
 * @author system
 * @date 2026-08-26
 */
@Data
@ApiModel("学科年级树视图对象")
public class AsSubjectGradeTreeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学科ID")
    private Long subjectId;

    @ApiModelProperty("学科名称（年级+学科）")
    private String subjectName;

}
