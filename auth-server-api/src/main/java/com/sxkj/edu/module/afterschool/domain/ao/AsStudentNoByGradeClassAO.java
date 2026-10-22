package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AsStudentNoByGradeClassAO {

    @NotBlank(message = "班级编号不能为空")
    @ApiModelProperty(value = "班级编号，2601班", required = true)
    private String className;

    @ApiModelProperty(value = "年级名称，一年级", required = true)
    @NotBlank(message = "年级编号不能为空")
    private String gradeName;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty(value = "学号最后两位",required = true)
    @NotBlank(message = "学号最后两位不能为空")
    @Length(max = 2, min = 2, message = "学号最后两位长度不能超过2")
    private String studentNoKeyword;
}
