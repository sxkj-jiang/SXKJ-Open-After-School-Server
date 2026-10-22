package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 意见反馈新增/编辑参数
 *
 * @author system
 * @date 2026-06-20
 */
@Data
@ApiModel("意见反馈新增/编辑参数")
public class AsContentAdviceAddAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("意见ID(编辑时必填)")
    private Long id;

    @NotNull(message = "来源不能为空")
    @ApiModelProperty(value = "来源：1-教师端、2-家长端、3-班主任", required = true)
    private Integer sourceType;

    @NotBlank(message = "意见内容不能为空")
    @ApiModelProperty(value = "意见内容", required = true)
    private String adviceName;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty(value = "分类ID,来源字典",required = true)
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @ApiModelProperty("反馈人")
    private String author;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("附件地址id,多个用,拼接")
    private String fileUrl;

}
