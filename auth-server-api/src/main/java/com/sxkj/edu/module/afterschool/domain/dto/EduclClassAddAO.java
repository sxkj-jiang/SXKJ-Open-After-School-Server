package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 班级新增/更新参数
 *
 * @author system
 * @date 2026-05-19
 */
@Data
@ApiModel("班级新增/更新参数")
public class EduclClassAddAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("班级ID(更新时必填)")
    private Long id;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "所属学校ID", required = true)
    private Long schoolId;

    @NotBlank(message = "班级名称不能为空")
    @ApiModelProperty(value = "班级名称", required = true)
    private String className;

    @NotNull(message = "年级ID不能为空")
    @ApiModelProperty(value = "年级ID", required = true)
    private Long gradeId;

    @NotNull(message = "学期ID不能为空")
    @ApiModelProperty(value = "学期ID", required = true)
    private Long termId;

    @ApiModelProperty("班主任ID")
    private Long headTeacherId;

    @NotNull(message = "入学年份不能为空")
    @ApiModelProperty(value = "入学年份",required = true)
    private Integer enrollmentYear;

    @NotNull(message = "毕业年份不能为空")
    private Integer graduationYear;

    @ApiModelProperty("备注")
    private String remark;
}
