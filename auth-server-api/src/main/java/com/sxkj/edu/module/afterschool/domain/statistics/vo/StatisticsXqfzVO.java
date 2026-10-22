package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class StatisticsXqfzVO {

    @ApiModelProperty("雷达图集合数据")
    private List<StatisticsCommonCountVO> ldtData;

    @ApiModelProperty("最热门课程")
    private String hotCourseNames;

    @ApiModelProperty("参与度最高年级")
    private String hotGradeName;

    @ApiModelProperty("课后服务总参与度")
    private String totalParticipationRate;
}
