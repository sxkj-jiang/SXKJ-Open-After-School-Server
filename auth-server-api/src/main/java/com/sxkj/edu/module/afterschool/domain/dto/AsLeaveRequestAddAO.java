package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("请假申请提交对象")
public class AsLeaveRequestAddAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生ID", required = true)
    @NotNull(message = "学生不能为空")
    private Long studentId;

    @ApiModelProperty(value = "学生身份ID", required = true)
    private Long studentIdentityId;

    @ApiModelProperty(value = "学生学号", required = true)
    @NotNull(message = "学生学号不能为空")
    private String studentNo;

    @ApiModelProperty(value = "请假类型 1病假 2事假 3其他", required = true)
    @NotNull(message = "请假类型不能为空")
    private Integer leaveType;

    @ApiModelProperty(value = "请假开始时间，格式：yyyy-MM-dd HH:mm", required = true)
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date leaveStartTime;

    @ApiModelProperty(value = "请假结束时间，格式：yyyy-MM-dd HH:mm", required = true)
    @NotNull(message = "结束时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date leaveEndTime;

    @ApiModelProperty(value = "请假天数，后端计算")
    private BigDecimal leaveDays;

    @ApiModelProperty(value = "请假原因")
    private String leaveReason;

    @ApiModelProperty(value = "请假证明附件id，多个用&拼接")
    private String leaveReasonFile;

    @ApiModelProperty(value = "请假影响的课程集合(课次)，格式：[1,2,3]",required = true)
    @NotEmpty(message = "请假影响的课程集合不能为空")
    private List<Long> leaveSessionList;
}
