package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 课后资源新增/更新AO
 *
 * @author wangyouhuai
 * @date 2026-06-17
 */
@Data
@ApiModel("课后资源新增/更新对象")
public class AsResourceAddAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "资源名称不能为空")
    @ApiModelProperty(value = "资源名称", required = true)
    private String resourceName;

    @ApiModelProperty(value = "封面文件ID")
    private Long coverFileId;

    @ApiModelProperty(value = "资源简介")
    private String resourceIntr;

}
