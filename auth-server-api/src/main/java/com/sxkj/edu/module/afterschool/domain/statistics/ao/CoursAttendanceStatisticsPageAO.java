package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("课程出勤预警分页查询参数")
public class CoursAttendanceStatisticsPageAO extends PageQuery {

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "学期ID不能为空")
    @ApiModelProperty(value = "学期ID", required = true)
    private Long termId;

    @ApiModelProperty(value = "预警阈值（20% = 20）", required = true)
    @NotNull(message = "预警阈值不能为空")
    private BigDecimal warningThreshold;

    @ApiModelProperty(value = "课程班名称/教师名称")
    private String queryInfo;
}
