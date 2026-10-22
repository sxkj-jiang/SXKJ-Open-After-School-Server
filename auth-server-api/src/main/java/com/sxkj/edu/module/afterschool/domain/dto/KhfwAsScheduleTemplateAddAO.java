package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.validation.ChineseAnno;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 课后时间管理方案新增/编辑参数
 *
 * @author YH
 * @date 2026-05-22
 */
@Data
@ApiModel("课后时间管理方案新增/编辑参数")
public class KhfwAsScheduleTemplateAddAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("方案ID(编辑时必填)")
    private Long id;

    @ApiModelProperty(value = "所属学校ID", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty(value = "方案名称", required = true)
    @NotBlank(message = "方案名称不能为空")
    @ChineseAnno
    private String templateName;

    @ApiModelProperty(value = "方案类型：2课后时间管理", required = true)
    @NotNull(message = "方案类型不能为空")
    private Integer templateType;

    @ApiModelProperty(value = "课后时间频率", required = true)
    @NotEmpty(message = "课后时间频率不能为空")
    private List<String> dayScopeList;

    @ApiModelProperty(value = "适用年级集合", required = true)
    @NotEmpty(message = "适用年级不能为空")
    private List<Long> gradeScopeList;

    @ApiModelProperty(value = "节次列表", required = true)
    @NotEmpty(message = "节次列表不能为空")
    @Valid
    private List<AsSchedulePeriodAO> periodList;

}
