package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 整改单视图对象
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("整改单视图对象")
public class AsRectificationOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("整改单ID")
    private Long id;

    @ApiModelProperty("关联巡课记录ID")
    private Long patrolRecordId;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("问题类型（字典值）")
    private String problemType;

    @ApiModelProperty("问题描述")
    private String problemDesc;

    @ApiModelProperty("责任教师ID")
    private Long responsibleTeacherId;

    @ApiModelProperty("责任教师姓名")
    private String responsibleTeacherName;

    @ApiModelProperty("整改状态：0待整改 1整改中 2已整改 3已完成 4已逾期")
    private Integer rectifyStatus;

    @ApiModelProperty("整改截止时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deadlineTime;

    @ApiModelProperty("整改措施")
    private String rectifyMeasure;

    @ApiModelProperty("整改意见")
    private String rectifyOpinion;

    @ApiModelProperty("问题附件ID集合JSON")
    private String questFileIds;

    @ApiModelProperty("整改后附件ID集合JSON")
    private String rectifyFileIds;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
