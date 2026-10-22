package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateFileForCourseSessionAo {

    @ApiModelProperty(value = "课次id", required = true)
    @NotNull(message = "课次id不能为空")
    private Long sessionId;

    @ApiModelProperty(value = "文件名id", required = true)
    @NotNull(message = "文件名id不能为空")
    private Long id;

    @ApiModelProperty(value = "资源名称",required = true)
    @NotBlank(message = "资源名称不能为空")
    private String resourceName;

    @ApiModelProperty(value = "资源描述")
    private String resourceIntr;

    @ApiModelProperty(value = "封面文件ID")
    private Long coverFileId;

}
