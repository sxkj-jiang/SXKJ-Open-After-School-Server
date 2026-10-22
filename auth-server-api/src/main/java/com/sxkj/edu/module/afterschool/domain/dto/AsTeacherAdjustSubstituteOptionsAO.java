package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 管理端按已选代课课次查询可用代课教师参数。
 */
@Data
@ApiModel("管理端按已选代课课次查询可用代课教师参数")
public class AsTeacherAdjustSubstituteOptionsAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前选中的实际代课课次ID集合；为空时使用教师原申请课次")
    private List<Long> sessionIds;

    @ApiModelProperty("代课周期开始日期，yyyy-MM-dd；sessionIds为空时按日期范围换算课次")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date adjustStartDate;

    @ApiModelProperty("代课周期结束日期，yyyy-MM-dd；sessionIds为空时按日期范围换算课次")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date adjustEndDate;
}
