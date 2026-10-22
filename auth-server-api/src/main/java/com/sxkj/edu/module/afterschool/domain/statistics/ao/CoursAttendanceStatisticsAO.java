package com.sxkj.edu.module.afterschool.domain.statistics.ao;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("课程出勤统计查询参数")
public class CoursAttendanceStatisticsAO{

    @NotNull(message = "类型不能为空")
    @ApiModelProperty(value = "类型：0-全部，1-出勤概览，2-出勤率趋势分析", required = true)
    private Integer type;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID",required = true)
    private Long schoolId;

    @NotNull(message = "学期ID不能为空")
    @ApiModelProperty(value = "学期ID",required = true)
    private Long termId;

    @ApiModelProperty(value = "预警阈值（20% = 20）",required = true)
    @NotNull(message = "预警阈值不能为空")
    private BigDecimal warningThreshold;
}
