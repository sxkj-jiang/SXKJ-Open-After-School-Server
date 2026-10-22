package com.sxkj.edu.module.afterschool.domain.ao.evaluation;

import com.sxkj.edu.common.validation.ChineseTitleAnno;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评价任务新增/更新AO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("评价任务新增/更新对象")
public class  AsEvaluationTaskAddAO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("任务ID（更新时必填")
    private Long id;
    
    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "学期ID不能为空")
    @ApiModelProperty(value = "学期ID", required = true)
    private Long termId;
    
    @NotNull(message = "模板ID不能为空")
    @ApiModelProperty(value = "模板ID", required = true)
    private Long templateId;
    
    @NotBlank(message = "任务名称不能为空")
    @ApiModelProperty(value = "任务名称", required = true)
    @ChineseTitleAnno
    private String taskName;

    @ApiModelProperty(value = "任务描述")
    private String taskDesc;


    @ApiModelProperty(value = "课程班ID集合（多选）")
    private List<Long> courseClassIds;
    
    @NotNull(message = "评价角色不能为空")
    @ApiModelProperty(value = "评价角色（角色ID）", required = true)
    private Long applicableRole;
    
    @NotNull(message = "适用场景不能为空")
    @ApiModelProperty(value = "适用场景ID集合", required = true)
    private List<Long> applicableScene;
    
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    
    @ApiModelProperty(value = "任务开始时提醒：0-不提醒 1-提醒")
    private Integer remindStart;
    
    @ApiModelProperty(value = "任务中期提醒：0-不提醒 1-提醒")
    private Integer remindMiddle;
    
    @ApiModelProperty(value = "任务结束前提醒：0-不提醒 1-提醒")
    private Integer remindEnd;

    @ApiModelProperty(value = "是否直接发布：0-保存草稿 1-直接发布，默认0")
    private Integer publishNow;
}
