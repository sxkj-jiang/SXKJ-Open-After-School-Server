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
 * 学生课次分页查询请求参数
 *
 * @author system
 * @date 2026-05-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("学生课次分页查询请求参数")
public class AsStudentSessionLeaderPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "学生不能为空")
    @ApiModelProperty(value = "学生ID", required = true)
    private Long studentId;

    @ApiModelProperty("学期ID；为空时使用学校默认学期")
    private Long termId;

    @ApiModelProperty(value = "课次开始日期，格式：yyyy-MM-dd HH:mm")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date sessionStartDate;

    @ApiModelProperty(value = "课次结束日期，格式：yyyy-MM-dd HH:mm")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date sessionEndDate;
}
