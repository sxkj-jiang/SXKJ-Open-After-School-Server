package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.validation.ChineseAnno;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 作息管理方案新增/编辑参数
 *
 * @author YH
 * @date 2026-05-22
 */
@Data
@ApiModel("作息管理方案新增/编辑参数")
public class ZxglAsScheduleTemplateAddAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("方案ID(编辑时必填)")
    private Long id;

    @ApiModelProperty(value = "所属学校ID", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty(value = "作息名称", required = true)
    @NotBlank(message = "作息名称不能为空")
    @ChineseAnno
    private String templateName;

    @ApiModelProperty(value = "方案类型：1作息管理 2课后时间管理", required = true)
    @NotNull(message = "方案类型不能为空")
    private Integer templateType;

//    @ApiModelProperty(value = "启用状态：1启用 0停用", required = true)
//    @NotNull(message = "启用状态不能为空")
//    private Integer enabledStatus;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "节次列表", required = true)
    @NotEmpty(message = "节次列表不能为空")
    @Valid
    private List<AsSchedulePeriodAO> periodList;
}
