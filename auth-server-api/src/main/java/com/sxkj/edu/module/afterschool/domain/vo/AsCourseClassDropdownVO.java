package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课后服务课程班下拉选项
 */
@Data
@ApiModel("课后服务课程班下拉选项")
public class AsCourseClassDropdownVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程班ID")
    private Long id;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("服务ID")
    private Long serviceId;
}
