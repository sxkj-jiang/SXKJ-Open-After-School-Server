package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("年级新增/更新对象")
public class EduclGradeAddAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("年级ID")
    private Long id;

    @ApiModelProperty("学校ID")
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "学段",required = true)
    @NotNull(message = "学段不能为空")
    private Integer schoolStage;

    @ApiModelProperty(value = "年级名称",required = true)
    @NotBlank(message = "年级名称不能为空")
    private String gradeName;

    @ApiModelProperty("年级主任教师ID")
    private Long directorUserId;

    @ApiModelProperty("年级主任名称")
    private String directorName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("排序号")
    private Integer sortNo;
}
