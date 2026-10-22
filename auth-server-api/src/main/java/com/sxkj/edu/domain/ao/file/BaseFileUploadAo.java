package com.sxkj.edu.domain.ao.file;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description: BaseFileUploadAo
 * @Author yanghui
 * @Date 2024年12月27日
 */
@Data
public class BaseFileUploadAo  {

    @ApiModelProperty(value = "文件类型：0:图片，1：视频，2-文档,3-其他",required = true)
    @NotNull(message = "文件类型不能为空")
    private Integer fileType;

    @ApiModelProperty("文件后缀名")
    private String fileSuffix;

    @ApiModelProperty(value = "是否敏感文件,1-是，0-否")
    private Integer isSensitive;
}
