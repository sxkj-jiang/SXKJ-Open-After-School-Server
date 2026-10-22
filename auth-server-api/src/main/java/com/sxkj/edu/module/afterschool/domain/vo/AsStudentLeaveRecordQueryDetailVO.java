package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 学生请假记录VO
 *
 * @author system
 * @date 2026-05-30
 */
@Data
@ApiModel("学生请假记录详情VO")
public class AsStudentLeaveRecordQueryDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("请假ID")
    private Long id;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("请假开始日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date leaveStartDate;

    @ApiModelProperty("请假结束日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date leaveEndDate;

    @ApiModelProperty("申请日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    @ApiModelProperty("请假完整日期")
    private String leaveFullDate;

    @ApiModelProperty("请假类型：1病假 2事假 3其他")
    private Integer leaveType;

    @ApiModelProperty("请假类型描述")
    private String leaveTypeDesc;

    @ApiModelProperty("请假原因")
    private String leaveReason;

    @ApiModelProperty(value = "请假天数,最小0.5天")
    private BigDecimal leaveDays;

    @ApiModelProperty("审批状态：0待审批 1已通过 2已驳回 3已撤回")
    private Integer approveStatus;

    @ApiModelProperty("年级")
    private String gradeName;

    @ApiModelProperty("班级")
    private String className;

    @ApiModelProperty(value = "请假附件地址集合")
    private List<String> leaveReasonFileList;

    @ApiModelProperty("请假课次集合")
    private List<AsLeaveRequestManagerQueryCourseDetVO> leaveSessionNameList;

}
