package com.sxkj.edu.domain.ao.wp;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryFileLabelPageAo extends PageParamDTO {


    @ApiModelProperty(value = "关键字搜索：包含文件名和上传人")
    private String keyword;

    @ApiModelProperty(value = "标签名称")
    @NotNull(message = "标签id不能为空")
    private Long labelId;

}
