package com.sxkj.edu.domain.base.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class TBaseExamRecordAo {

    @ApiModelProperty(value = "原表主键id",required = true)
    private Long originId;

    @ApiModelProperty(value = "审核人",required = true)
    private String examUser;

    @ApiModelProperty(value = "审核意见",required = true)
    @NotBlank(message = "审核意见不能为空")
    private String examAdvice;

    @ApiModelProperty(value = "审核状态：1-已审核，2-待审核，3-已拒绝,4-草稿",required = true)
    @NotNull(message = "审核状态不能为空")
    private Integer examStatus;

    @ApiModelProperty(value = "审核日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date examDate;

    public TBaseExamRecordAo(Long originId, String examUser, String examAdvice, Integer examStatus, Date examDate) {
        this.originId = originId;
        this.examUser = examUser;
        this.examAdvice = examAdvice;
        this.examStatus = examStatus;
        this.examDate = examDate;
    }

    public TBaseExamRecordAo(){}
}
