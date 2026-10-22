package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 问题整改单子项（新增/编辑巡课记录时携带）
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("问题整改单子项")
public class RectificationOrderItemAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("整改单ID（编辑时必填）")
    private Long id;

    @ApiModelProperty(value = "问题类型（字典值）",required = true)
    private String problemType;

    @ApiModelProperty(value = "问题描述", required = true)
    private String problemDesc;

    @ApiModelProperty(value = "责任教师ID（对应 t_as_teacher.id）", required = true)
    private Long responsibleTeacherId;

    @ApiModelProperty(value = "整改截止时间", required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date deadlineTime;

    @ApiModelProperty("整改意见")
    private String rectifyOpinion;

    @ApiModelProperty("问题附件ID集合JSON")
    private String questFileIds;
}
