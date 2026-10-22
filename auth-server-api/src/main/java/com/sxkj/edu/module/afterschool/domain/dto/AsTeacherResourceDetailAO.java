package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 教师课程资源详情查询AO
 *
 * @author jiangjj
 * @date 2026-07-07
 */
@Data
@ApiModel("教师课程资源详情查询对象")
public class AsTeacherResourceDetailAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "资源ID不能为空")
    @ApiModelProperty(value = "资源ID", required = true)
    private Long resourceId;

    @NotNull(message = "课次ID不能为空")
    @ApiModelProperty(value = "课次ID", required = true)
    private Long sessionId;

}
