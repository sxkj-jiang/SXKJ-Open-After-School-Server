package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 教师课表查询参数
 *
 * @author system
 * @date 2026-05-29
 */
@Data
@ApiModel("教师课表查询参数")
public class AsTeacherScheduleQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "学校ID", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "教师ID", required = true)
    @NotNull(message = "教师ID不能为空")
    private Long teacherId;

    @ApiModelProperty(value = "查询类型：1周 2月 3天", required = true)
    @NotNull(message = "查询类型不能为空")
    private Integer queryType;

    @ApiModelProperty(value = "查询日期（周查询时为当周的任意一天，月查询时为当月的任意一天，天查询时为指定日期）,yyyy-mm-dd", required = true)
    @NotBlank(message = "查询日期不能为空")
    private String queryDate;
}
