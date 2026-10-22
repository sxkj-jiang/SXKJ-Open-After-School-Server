package com.sxkj.edu.module.afterschool.domain.vo;

import com.sxkj.edu.domain.vo.StatisticKeyWordPercentageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 请假统计视图对象
 *
 * @author system
 * @date 2026-05-22
 */
@Data
@ApiModel("请假统计视图对象")
public class AsLeaveRequestStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("总请假人数")
    private Integer totalLeaveStudentCount;

    @ApiModelProperty("请假总次数")
    private Integer totalLeaveCount;

    @ApiModelProperty("高频请假人数（时间范围内超过5次）")
    private Integer highFrequencyLeaveCount;

    @ApiModelProperty("请假类型分布占比")
    private List<StatisticKeyWordPercentageVO> leaveTypeDistribution;

    @ApiModelProperty("班级请假率统计")
    private List<AsLeaveRequestClassRateVO> classLeaveRateList;
}
