package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生课次VO
 *
 * @author system
 * @date 2026-05-30
 */
@Data
@ApiModel("学生课次VO")
public class AsStudentSessionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课次日期,格式yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date sessionDate;

    @ApiModelProperty("上课开始时间,格式HH:mm")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date startTime;

    @ApiModelProperty("上课结束时间,格式HH:mm")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date endTime;
}
