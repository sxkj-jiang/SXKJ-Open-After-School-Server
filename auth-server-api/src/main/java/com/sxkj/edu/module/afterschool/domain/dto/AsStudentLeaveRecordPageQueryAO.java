package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 学生请假记录分页查询请求参数
 *
 * @author system
 * @date 2026-05-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("学生请假记录分页查询请求参数")
public class AsStudentLeaveRecordPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "家长ID不能为空")
    @ApiModelProperty(value = "家长ID", required = true)
    private Long guardianId;

    @ApiModelProperty("请假开始日期 yyyy-MM-dd HH:mm")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date leaveStartDate;

    @ApiModelProperty("请假结束日期 yyyy-MM-dd HH:mm")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date leaveEndDate;


    @ApiModelProperty(value = "请假类型：1病假 2事假 3其他")
    private Integer leaveType;

    @ApiModelProperty("学期ID，未传时默认使用学校默认学期")
    private Long termId;

}
