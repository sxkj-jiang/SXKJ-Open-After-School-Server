package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AsStuCourseFeedbackPageAO extends PageQuery {

    @ApiModelProperty(value = "学校ID",required = true)
    private Long schoolId;

    @ApiModelProperty("学期ID；存在课次ID时以后端解析的课次学期为准")
    private Long termId;

    @NotNull(message = "课次ID不能为空")
    @ApiModelProperty(value = "课次ID", required = true)
    private Long sessionId;

    @ApiModelProperty(value = "是否提交 0-否 1-是", required = true)
    private Integer isSubmit;


}
