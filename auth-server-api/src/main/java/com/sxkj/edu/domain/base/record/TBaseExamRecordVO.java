package com.sxkj.edu.domain.base.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TBaseExamRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("被审核表主键")
    private Long originId;

    @ApiModelProperty("审核人")
    private String examUser;

    @ApiModelProperty("审核类型")
    private String examType;

    @ApiModelProperty("审核意见")
    private String examAdvice;

    @ApiModelProperty("审核状态：1-已审核，2-待审核，3-已拒绝,4-其他")
    private Integer examStatus;

    @ApiModelProperty("审核时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date examDate;

}
