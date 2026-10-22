package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TPortalProtocolUpOrInsAO {

    @ApiModelProperty("协议主键id 修改必传")
    private Long id;

    @ApiModelProperty(value = "协议名称",required = true)
    @NotBlank(message = "协议名称不能为空")
    private String protocolName;

    @ApiModelProperty(value = "协议编号",required = true)
    @NotBlank(message = "协议编号不能为空")
    private String protocolCode;

    @ApiModelProperty(value = "协议类型 1-用户协议 2-隐私政策",required = true)
    @NotNull(message = "协议类型不能为空")
    private Integer protocolType;

    @ApiModelProperty(value = "版本号",required = true)
    @NotBlank(message = "版本号不能为空")
    private String versionNum;

    @ApiModelProperty(value = "是否启用 0-否 1-是",required = true)
    @NotNull(message = "是否启用不能为空")
    private Integer isEnable;

    @ApiModelProperty(value = "使用场景(多选) 1-用户注册 2-用户认证,3-其他",required = true)
    @NotEmpty(message = "使用场景不能为空")
    private List<Integer> useCaseList;

    @ApiModelProperty(value = "条款内容",required = true)
    @NotBlank(message = "条款内容不能为空")
    private String protocolContent;
}
