package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 课后服务复制入参。
 */
@Data
@ApiModel("课后服务复制入参")
public class AsServiceCopyAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "复制请求号，同一次提交和重试必须保持一致", required = true)
    @NotBlank(message = "复制请求号不能为空")
    @Size(max = 64, message = "复制请求号不能超过64个字符")
    private String requestNo;

    @ApiModelProperty(value = "复制后的课后服务名称", required = true)
    @NotBlank(message = "课后服务名称不能为空")
    @Size(max = 128, message = "课后服务名称不能超过128个字符")
    private String serviceName;

    @ApiModelProperty(value = "目标学期", required = true)
    @NotNull(message = "学期不能为空")
    private Long termId;

    @ApiModelProperty(value = "开课日期", required = true)
    @NotNull(message = "开课日期不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date courseStartDate;

    @ApiModelProperty(value = "结课日期", required = true)
    @NotNull(message = "结课日期不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date courseEndDate;

    @ApiModelProperty(value = "报名开始时间", required = true)
    @NotNull(message = "报名开始时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enrollStartTime;

    @ApiModelProperty(value = "报名结束时间", required = true)
    @NotNull(message = "报名结束时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enrollEndTime;
}
