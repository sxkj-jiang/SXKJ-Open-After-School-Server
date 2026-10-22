package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生课时详情-详细课时记录分页VO
 *
 * @author system
 * @date 2026-06-09
 */
@Data
@ApiModel("学生课时详情-详细课时记录分页VO")
public class StudentLessonRecordPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("上课日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sessionDate;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("上课时间（开始时间-结束时间）")
    private String sessionTime;

    @ApiModelProperty("出勤状态（1-出勤 2-迟到 3-早退 4-缺勤 5-请假，null-未记录）")
    private Integer attendanceStatus;

    @ApiModelProperty("备注")
    private String remark;
}
