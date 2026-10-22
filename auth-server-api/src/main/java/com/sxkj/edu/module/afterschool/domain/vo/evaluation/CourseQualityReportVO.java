package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.sxkj.edu.module.afterschool.domain.dto.EvaluationMonthTrendDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("课程质量报告VO")
public class CourseQualityReportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("综合评分")
    private BigDecimal comprehensiveScore;

    @ApiModelProperty("评分等级")
    private String scoreLevel;

    @ApiModelProperty("雷达图数据")
    private List<RadarDimensionVO> radarData;

    @ApiModelProperty("评分趋势")
    private List<EvaluationMonthTrendDTO> trendData;

    @ApiModelProperty("同类型课程对比")
    private List<CompareCourseVO> compareData;

    @Data
    public static class RadarDimensionVO implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("维度名称")
        private String dimensionName;

        @ApiModelProperty("得分")
        private BigDecimal score;
    }

    @Data
    public static class CompareCourseVO implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("课程名称")
        private String courseName;

        @ApiModelProperty("维度得分列表")
        private List<DimensionScoreVO> dimensionScores;
    }

    @Data
    public static class DimensionScoreVO implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("维度名称")
        private String dimensionName;

        @ApiModelProperty("得分（平均分）")
        private BigDecimal score;
    }
}
