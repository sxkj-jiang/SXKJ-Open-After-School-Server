package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 课后服务第一步基本信息保存对象。
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("课后服务基本信息保存对象")
public class AsServiceBasicSaveAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("服务ID，新增时为空")
    private Long id;

    @ApiModelProperty("学校ID，不传时取当前登录学校")
    private Long schoolId;

    @ApiModelProperty(value = "学期ID", required = true)
    @NotNull(message = "学期不能为空")
    private Long termId;

    @ApiModelProperty(value = "课后服务名称", required = true)
    @NotBlank(message = "课后服务名称不能为空")
    private String serviceName;

    // 0602确认报名时间都为必填
    @ApiModelProperty(value = "报名开始时间",required = true)
    @NotNull(message = "报名开始时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enrollStartTime;

    // 0602确认报名时间都为必填
    @ApiModelProperty(value = "报名结束时间",required = true)
    @NotNull(message = "报名结束时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enrollEndTime;

    @ApiModelProperty(value = "开课日期，根据选择学期带出", required = true)
    @NotNull(message = "开课日期不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date courseStartDate;

    @ApiModelProperty(value = "结课日期，根据选择学期带出", required = true)
    @NotNull(message = "结课日期不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date courseEndDate;

    @ApiModelProperty("课后时间方案ID")
    private Long afterSchoolTimeTemplateId;

    @ApiModelProperty("服务适用年级ID集合JSON（保留字段，业务暂不使用）")
    @Deprecated
    private String gradeScopeJson;

    @ApiModelProperty("服务适用行政班ID集合JSON（保留字段，业务暂不使用）")
    @Deprecated
    private String classScopeJson;
}
