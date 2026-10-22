package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 教师或班主任分页查询课次学生考勤参数。
 */
@Data
@ApiModel("教师分页查询课次学生考勤记录参数")
public class AsAttendanceStudentPageQueryAO extends PageQuery {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校ID", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "教师ID，后端根据登录用户自动填充")
    private Long teacherId;

    @ApiModelProperty(value = "课次时间", required = true)
    @NotNull(message = "课次时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date sessionDate;

    @ApiModelProperty(value = "第几节课",required = true)
    @NotNull(message = "第几节课不能为空")
    private Integer sectionNo;

    @ApiModelProperty(value = "课次ID (用于任课老师查看考勤)", required = true)
    private Long sessionId;

    @ApiModelProperty("学期ID，未传取默认上学期")
    private Long termId;
}
