package com.sxkj.edu.domain.ao.wp;

import com.sxkj.edu.common.validation.ChineseAnno;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SaveUpdateFileLabelAo {

    @ApiModelProperty(value = "标签编号，更新时必填")
    private Long id;

    @ApiModelProperty(value = "标签名，2-10位的中文组成",required = true)
    @NotBlank(message = "标签名不能为空")
    @ChineseAnno
    private String labelName;

    @ApiModelProperty(value = "标签颜色，如#fffff",required = true)
    @NotBlank(message = "标签颜色不能为空")
    private String colorValue;

    @ApiModelProperty(value = "标签类型")
    private String labelType;
}
