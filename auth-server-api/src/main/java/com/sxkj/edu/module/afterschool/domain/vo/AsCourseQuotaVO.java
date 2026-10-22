package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/** 公开课名额信息。原容量字段仍表示课程班整体容量。 */
@Data
public class AsCourseQuotaVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("年级限额是否完整配置，false 时不能新增报名")
    private Boolean configured;
    private List<AsCourseGradeQuotaVO> gradeQuotas;
    @ApiModelProperty("总占用人数，与名单展示人数分开，退出前始终占位")
    private Integer occupiedCount;
    private Integer remainingCount;
    @ApiModelProperty("服务所属学期中该学生的年级；无学生上下文时为空")
    private Long studentGradeId;
    private Integer gradeCapacity;
    private Integer gradeEnrolledCount;
    private Integer gradeRemainingCount;
    @ApiModelProperty("该学生实际可用名额=min(课程班剩余,年级剩余)，不能代替提交时的并发校验")
    private Integer availableCount;
}
