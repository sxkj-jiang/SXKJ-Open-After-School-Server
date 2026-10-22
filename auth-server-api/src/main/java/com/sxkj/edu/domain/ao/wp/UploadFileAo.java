package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UploadFileAo {


    @ApiModelProperty(value = "上传文件路径,格式为/测试",required = true)
    @NotBlank(message = "上传文件路径不能为空")
    private String filePath;

    @ApiModelProperty(value = "文件或目录名，格式为：1.jpg",required = true)
    @NotBlank(message = "文件或目录名不能为空")
    private String fileName;

    @ApiModelProperty("是否是目录(0-否,1-是)")
    private Integer isDir;

    @ApiModelProperty("上传角色，1-管理员,2-用户，用户上传需要最终确认")
    private Integer uploadStatus;

    @ApiModelProperty("总大小")
    private long totalSize;


}
