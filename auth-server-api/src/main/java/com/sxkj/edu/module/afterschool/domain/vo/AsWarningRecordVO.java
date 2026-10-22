package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("预警记录视图对象")
public class AsWarningRecordVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("预警ID")
    private Long id;
    @ApiModelProperty("学校ID")
    private Long schoolId;
    @ApiModelProperty("学校名称")
    private String schoolName;
    @ApiModelProperty("规则ID")
    private Long ruleId;
    @ApiModelProperty("规则名称")
    private String ruleName;
    @ApiModelProperty("学生ID")
    private Long studentId;
    @ApiModelProperty("学生姓名")
    private String studentName;
    @ApiModelProperty("学生学号")
    private String studentNo;
    @ApiModelProperty("课次ID")
    private Long sessionId;
    @ApiModelProperty("预警等级")
    private Integer warningLevel;
    @ApiModelProperty("预警等级描述")
    private String warningLevelDesc;
    @ApiModelProperty("预警状态")
    private Integer warningStatus;
    @ApiModelProperty("预警状态描述")
    private String warningStatusDesc;
    @ApiModelProperty("触发时间")
    private Date triggerTime;
    @ApiModelProperty("预警原因")
    private String warningReason;
    @ApiModelProperty("创建时间")
    private Date createTime;
}
