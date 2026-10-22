package com.sxkj.edu.domain.base.suggest.request;

import com.sxkj.edu.common.validation.Mobile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class SuggestAddReq implements Serializable {

    @ApiModelProperty(value = "反馈内容", required = true)
    @NotBlank(message = "反馈内容不能为空")
    private String adviceName;

    @ApiModelProperty(value = "意见来源，平台端、移动端", required = true)
    @NotBlank(message = "意见来源不能为空")
    private String sourceName;

    @ApiModelProperty(value = "手机号码",required = true)
    @NotBlank(message = "手机号码不能为空")
    @Mobile
    private String mobile;

    @ApiModelProperty(value = "反馈人id", required = true)
    @NotNull(message = "反馈人id不能为空")
    private Long userId;

    @ApiModelProperty(value = "分类，来源字典表", required = true)
    @NotNull(message = "反馈类型编号不能为空")
    private Long categoryId;

    @ApiModelProperty(value = "附件地址,多个用,拼接")
    private String fileUrl;

}
