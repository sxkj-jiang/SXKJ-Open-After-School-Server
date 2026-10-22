package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 教师课程资源状态查询AO
 *
 * @author jiangjj
 * @date 2026-07-07
 */
@Data
@ApiModel("教师课程资源状态查询对象")
public class AsTeacherResourceQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "课次ID不能为空")
    @ApiModelProperty(value = "课次ID", required = true)
    private Long sessionId;

    @ApiModelProperty(value = "资源名称（模糊查询）")
    private String resourceName;

}
