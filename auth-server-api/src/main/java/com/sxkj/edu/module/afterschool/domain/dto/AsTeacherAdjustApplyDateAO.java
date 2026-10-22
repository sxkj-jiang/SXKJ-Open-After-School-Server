package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 任课老师提交调课申请参数。
 */
@Data
@ApiModel("调课根据日期查询可调课的数据")
public class AsTeacherAdjustApplyDateAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("调课开始日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date adjustStartDate;

    @ApiModelProperty("调课结束日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date adjustEndDate;

}
